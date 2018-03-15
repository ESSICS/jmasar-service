package se.esss.ics.masar.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.services.IServices;


@RestController
public class ConfigurationController extends BaseController{
	
	@Autowired
	private IServices services;

	@ApiOperation(value = "Create a new configuration", consumes = "application/json;charset=UTF-8")
	@PutMapping("/config")
	public Config saveNewConfiguration(@RequestBody final Config configuration) {
		
		return services.saveNewConfiguration(configuration);
	}
	
	@ApiOperation(value = "Get all configurations", consumes = "application/json;charset=UTF-8")
	@GetMapping("/config")
	public List<Config> getConfigurations() {
		return services.getConfigs();
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
