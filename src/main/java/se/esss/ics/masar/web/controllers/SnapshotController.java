package se.esss.ics.masar.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;
import se.esss.ics.masar.services.IServices;

@RestController
public class SnapshotController extends BaseController {

	@Autowired
	private IServices services;

	@ApiOperation(value = "Take a snapshot, i.e. save preliminary.")
	@PutMapping("/snapshot/{configId}")
	public Snapshot takeSnapshot(@PathVariable int configId) {
		return services.takeSnapshot(configId);
	}

	@ApiOperation(value = "Retrieve snapshot values")
	@GetMapping("/snapshot/{snapshotId}/values")
	public <T> List<SnapshotPv<T>> getSnapshotPvs(@PathVariable int snapshotId) {
		return services.getSnapshotPvValues(snapshotId);
	}

	@ApiOperation(value = "Get a snapshot", consumes = "application/json;charset=UTF-8")
	@GetMapping("/snapshot/{snapshotId}")
	public Snapshot getSnapshot(@PathVariable int snapshotId) {

		return services.getSnapshot(snapshotId);
	}

	@ApiOperation(value = "Delete a snapshot", consumes = "application/json;charset=UTF-8")
	@DeleteMapping("/snapshot/{snapshotId}")
	public void deleteSnapshot(@PathVariable int snapshotId) {

		services.deleteSnapshot(snapshotId);
	}

	@ApiOperation(value = "Commit a snapshot, i.e. update with user name and comment.")
	@PostMapping("/snapshot/{snapshotId}")
	public Snapshot commitSnapshot(@PathVariable int snapshotId, @RequestParam(required = true) String userName,
			@RequestParam(required = true) String comment) {

		return services.commitSnapshot(snapshotId, userName, comment);
	}
}
