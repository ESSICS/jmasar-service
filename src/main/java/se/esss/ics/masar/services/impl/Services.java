/** 
 * Copyright (C) ${year} European Spallation Source ERIC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.esss.ics.masar.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.exception.ConfigNotFoundException;
import se.esss.ics.masar.services.exception.SnapshotNotFoundException;


public class Services implements IServices{

	@Autowired
	private ConfigDAO configDAO;
	
	
	@Autowired
	private SnapshotDAO snapshotDAO;
	
	@Autowired
	private IEpicsService epicsService;
	
	private Logger logger = LoggerFactory.getLogger(Services.class.getName());
	
	@Override
	@Transactional
	public Config createNewConfiguration(Config config) {
		
		if(config.getParent() == null) {
			throw new IllegalArgumentException("Parent of configuration not specified");
		}
		return configDAO.createConfiguration(config);
	}
	
	@Override
	public Config getConfiguration(int nodeId) {
		
		return configDAO.getConfiguration(nodeId);
	}
	
	@Override
	@Transactional
	public Snapshot takeSnapshot(int nodeId) {
		
		Config config = configDAO.getConfiguration(nodeId);
		
		if(config == null) {
			throw new ConfigNotFoundException("Configuration with id=" + nodeId + " does not exist.");
		}
		
		List<SnapshotPv<?>> snapshotPvs = new ArrayList<>();
		
		for(ConfigPv configPv : config.getConfigPvList()) {
			try {
				snapshotPvs.add(epicsService.getPv(configPv));
			} catch (PVReadException e) {
				logger.error(e.getMessage());
			}
		}
		
		Snapshot snapshot = Snapshot.builder()
				.configId(nodeId)
				.snapshotPvList(snapshotPvs)
				.build();
		
		return configDAO.savePreliminarySnapshot(snapshot);
	
	}
		
	@Override
	public Snapshot commitSnapshot(int snapshotId, String userName, String comment) {
		snapshotDAO.commitSnapshot(snapshotId, userName, comment);
		
		return snapshotDAO.getSnapshot(snapshotId, true);
	}
	
	@Override
	public void deleteSnapshot(int snapshotId) {
		snapshotDAO.deleteSnapshot(snapshotId);
	}
	
	@Override
	public List<Snapshot> getSnapshots(int configId){
		return snapshotDAO.getSnapshots(configId);
	}
	
	@Override
	public Snapshot getSnapshot(int snapshotId){
		Snapshot snapshot = snapshotDAO.getSnapshot(snapshotId, true);
		if(snapshot == null) {
			throw new SnapshotNotFoundException("Snapshot with id=" + snapshotId  + " not found.");
		}
		return snapshot;
	}
	
	@Override
	public Folder createFolder(Folder folder) {
		
		if(folder.getParent() == null) {
			throw new IllegalArgumentException("Cannot create new folder as parent folder is not specified.");
		}
	
		return configDAO.createFolder(folder);
	}
	
	@Override
	public Folder getFolder(int nodeId) {
		return  configDAO.getFolder(nodeId);
	}
	
	
	@Override
	@Transactional
	public Folder moveNode(int nodeId, int targetNodeId) {
		return configDAO.moveNode(nodeId, targetNodeId);
	}
	
	@Override
	@Transactional
	public void deleteNode(int nodeId) {
		configDAO.deleteNode(nodeId);
	}
	
	@Override
	@Transactional
	public Config updateConfiguration(Config config) {
	
		return configDAO.updateConfiguration(config);
	}
	
	@Override
	public Node renameNode(int nodeId, String name) {
		return configDAO.renameNode(nodeId, name);
	}
}
