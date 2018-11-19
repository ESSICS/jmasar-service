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
package se.esss.ics.masar.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.services.IServices;


@RestController
public class ConfigurationController extends BaseController{
	
	@Autowired
	private IServices services;

	/**
	 * Create a new folder in the tree structure.
	 * @param node A {@link Folder} object. The name and parent fields must be non-null.
	 * @return The folder inserted into the tree.
	 */
	@ApiOperation(value = "Create a new folder", consumes = JSON, produces = JSON)
	@PutMapping("/folder")
	public Folder createFolder(@RequestBody final Folder folder) {
		return services.createFolder(folder);
	}
	
	@ApiOperation(value = "Delete a folder and its sub-tree")
	@DeleteMapping("/folder/{nodeId}")
	public void deleteFolder(@PathVariable final int nodeId) {
		 services.deleteNode(nodeId);
	}
	
	/**
	 * Get a folder.
	 * @param nodeId The database id of the folder.
	 * @return A {@link Folder} object or <code>null</code> if the node id is not associated with an 
	 * existing folder. The returned object will contain existing child nodes as well as the parent node,
	 * which is <code>null</code> for the root folder.
	 */
	@ApiOperation(value = "Get a folder and its child nodes", produces = JSON)
	@GetMapping("/folder/{nodeId}")
	public Folder getFolder(@PathVariable final int nodeId) {
		return services.getFolder(nodeId);
	}

	@ApiOperation(value = "Create a new configuration", consumes = JSON)
	@PutMapping("/config")
	public Config saveConfiguration(@RequestBody final Config configuration) {
		return services.createNewConfiguration(configuration);
	}
	
	@ApiOperation(value = "Get configuration and its list of PVs", produces = JSON)
	@GetMapping("/config/{nodeId}")
	public Config getConfiguration(@PathVariable final int nodeId) {
		return services.getConfiguration(nodeId);
	}
	
	/**
	 * Updates a configuration. For instance, user may change the name of the configuration or modify the list of PVs. NOTE: in case PVs are removed from
	 * the configuration, the corresponding snapshot values are also deleted.
	 * @param nodeId The node id of the configuration.
	 * @param config The configuration object holding updated data (name, PV list...).
	 * @return
	 */
	@ApiOperation(value = "Update configuration (e.g. modify PV list or rename configuration)", consumes = JSON, produces = JSON)
	@PostMapping("/config")
	public Config updateConfiguration(@RequestBody Config config) {
		return services.updateConfiguration(config);
	}

	
	@ApiOperation(value = "Delete a configuration and all snapshots associated with it.")
	@DeleteMapping("/config/{nodeId}")
	public void deleteNode(@PathVariable final int nodeId) {
		services.deleteNode(nodeId);
	}
	
	
	@ApiOperation(value = "Get all snapshots for a config. NOTE: preliminary snapshots are not included.", produces = JSON)
	@GetMapping("/config/{nodeId}/snapshots")
	public List<Snapshot> getSnapshots(@PathVariable int nodeId) {
		return services.getSnapshots(nodeId);
	}
	
	@ApiOperation(value = "Moves a node (and the sub-tree in case of a folder node) to another target folder.", produces = JSON)
	@PostMapping("/node/{nodeId}")
	public Folder moveNode(@PathVariable int nodeId, @RequestParam(value = "to", required = true) int to) {
		return services.moveNode(nodeId, to);
	}
	
	@ApiOperation(value = "Renames a Node. The parent directory must not contain a node with same name and type.", produces = JSON)
	@PostMapping("/node/{nodeId}/rename")
	public Node renameNode(@PathVariable int nodeId, @RequestParam(value = "name", required = true) String name) {
		return services.renameNode(nodeId, name);
	}
}
