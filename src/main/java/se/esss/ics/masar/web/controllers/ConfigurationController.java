package se.esss.ics.masar.web.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.services.IServices;


@RestController
public class ConfigurationController extends BaseController{
	
	@Autowired
	private IServices services;
	

	/**
	 * Create a new "folder" in the tree structure.
	 * @param node A {@link Node} object describing the new node (folder). It's parent node must be non-null.
	 * @return The node inserted into the tree.
	 */
	@ApiOperation(value = "Create a new folder", consumes = "application/json;charset=UTF-8")
	@PutMapping("/folder")
	public Node createNewFolder(@RequestBody final Node node) {
		
		return services.createNewFolder(node);
	}

	@ApiOperation(value = "Create a new configuration", consumes = "application/json;charset=UTF-8")
	@PutMapping("/config")
	public Node saveNewConfiguration(@Valid @RequestBody final Config configuration) {
		
		return services.createNewConfiguration(configuration);
	}
	
	@ApiOperation(value = "Get a node including its child nodes, or get a config inlcuding PVs")
	@GetMapping("/node/{nodeId}")
	public Node getNode(@PathVariable final int nodeId) {
		
		return services.getNode(nodeId);
	}
	
	@ApiOperation(value = "Get PVs from config id", consumes = "application/json;charset=UTF-8")
	@GetMapping("/config/{configId}")
	public Config getConfiguration(@PathVariable int configId) {
		return services.getConfig(configId);
	}
	
	@ApiOperation(value = "Get all snapshots from config id", consumes = "application/json;charset=UTF-8")
	@GetMapping("/config/{configId}/snapshots")
	public List<Snapshot> getSnapshots(@PathVariable int configId) {
		return services.getSnapshots(configId);
	}
}
