package se.esss.ics.masar.web.controllers;

import java.util.List;

import javax.validation.Valid;

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
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.services.IServices;


@RestController
public class ConfigurationController extends BaseController{
	
	@Autowired
	private IServices services;
	

	/**
	 * Create a new "folder" in the tree structure.
	 * @param node A {@link Folder} object. The name and parent fields must be non-null.
	 * @return The folder inserted into the tree.
	 */
	@ApiOperation(value = "Create a new folder", consumes = "application/json;charset=UTF-8")
	@PutMapping("/folder")
	public Folder createFolder(@RequestBody final Folder folder) {
		
		return services.createFolder(folder);
	}
	
	@ApiOperation(value = "Delete a folder and its sub-tree")
	@DeleteMapping("/folder/{nodeId}")
	public void deleteFolder(@PathVariable final int nodeId) {
		
		 services.deleteFolder(nodeId);
	}
	
	/**
	 * Get a folder.
	 * @param nodeId The database id of the folder.
	 * @return A {@link Folder} object or <code>null</code> if the node id is not associated with an 
	 * existing folder. The returned object will contain existing child nodes as well as the parent node,
	 * which is <code>null</code> for the root folder.
	 */
	@ApiOperation(value = "Get a folder and its child nodes", produces = "application/json;charset=UTF-8")
	@GetMapping("/folder/{nodeId}")
	public Folder getFolder(@PathVariable final int nodeId) {
		
		return services.getFolder(nodeId);
	}

	@ApiOperation(value = "Create a new configuration", consumes = "application/json;charset=UTF-8")
	@PutMapping("/config")
	public Config saveConfiguration(@Valid @RequestBody final Config configuration) {
		
		return services.createNewConfiguration(configuration);
	}
	
	@ApiOperation(value = "Get configuration and its list of PVs", produces = "application/json;charset=UTF-8")
	@GetMapping("/config/{nodeId}")
	public Config getConfiguration(@PathVariable final int nodeId) {
		
		return services.getConfiguration(nodeId);
	}

	
	@ApiOperation(value = "Delete a configuration and all snapshots associated with it.")
	@DeleteMapping("/config/{nodeId}")
	public void deleteNode(@PathVariable final int nodeId) {
		
		services.deleteConfiguration(nodeId);
	}
	
	
	@ApiOperation(value = "Get all snapshots for a config. NOTE: preliminary snapshots are not included.", produces = "application/json;charset=UTF-8")
	@GetMapping("/config/{nodeId}/snapshots")
	public List<Snapshot> getSnapshots(@PathVariable int nodeId) {
		return services.getSnapshots(nodeId);
	}
	
	@ApiOperation(value = "Moves a node (and the sub-tree in case of a folder node) to another target folder.", produces = "application/json;charset=UTF-8")
	@PostMapping("/node/{nodeId}")
	public Folder moveNode(@PathVariable int nodeId, @RequestParam(value = "to") int targetNodeId) {
		return services.moveNode(nodeId, targetNodeId);
	}
}
