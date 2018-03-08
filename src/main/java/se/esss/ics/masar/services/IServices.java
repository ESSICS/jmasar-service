package se.esss.ics.masar.services;

import java.util.List;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;

public interface IServices {
	
	public Config saveNewConfiguration(Config configuration);
	
	public List<Config> getConfigs();
	
	public Config getConfig(int configId);
	
	public int takeSnapshot(int configId);
	
	public Snapshot commitSnapshot(int snapshotId, String userName, String comment);
	
	public List<Snapshot> getSnapshots(int configId);
	
	public Snapshot getSnapshot(int snapshotId);
	
	public List<SnapshotPv> getSnapshotPvs(int snapshotId);
	
	public void deleteSnapshot(int snapshotId);
}
