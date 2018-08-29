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
package se.esss.ics.masar.epics.impl;

import org.epics.pvaClient.PvaClient;
import org.epics.pvaClient.PvaClientGetData;
import org.epics.pvdata.pv.PVStructure;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.exception.PVReadException;
import se.esss.ics.masar.epics.util.SnapshotPvFactory;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

public class EpicsService implements IEpicsService {

	@Autowired
	private PvaClient pvaClient;

	@Override
	public <T> SnapshotPv<T> getPv(ConfigPv configPv) throws PVReadException {

		PvaClientGetData pvaClientGetData;
		try {
			pvaClientGetData = pvaClient.channel(configPv.getPvName(), "ca", 3.0).get().getData();
			PVStructure myPVStructure = pvaClientGetData.getPVStructure();
			return SnapshotPvFactory.createSnapshotPv(configPv, myPVStructure);
		} catch (Exception e1) {
			LoggerFactory.getLogger(EpicsService.class).error(e1.getMessage());
			return SnapshotPv.<T>builder().fetchStatus(false).configPv(configPv).build();
		}		
	}
}
