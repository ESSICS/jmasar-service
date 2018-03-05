package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.snapshot.Snapshot;

public interface SnapshotDAO {

	public int savePreliminarySnapshot(Snapshot snapshot);
	
	public List<Snapshot> getSnapshots(int configId);
	
	public Snapshot getSnapshot(int snapshotId);
	
	public void commitSnapshot(int snapshotId, String userName, String comment);
	
	public void deleteSnapshot(int snapshotId);
	
}
