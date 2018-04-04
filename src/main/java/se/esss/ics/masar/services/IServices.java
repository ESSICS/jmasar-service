package se.esss.ics.masar.services;

import java.util.List;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;

public interface IServices {
	
	public Node createNewFolder(Node node);
	
	public Node createNewConfiguration(Config configuration);
	
	public Node getNode(int nodeId);
	
	public Config getConfig(int configId);
	
	public Snapshot takeSnapshot(int configId);
	
	public Snapshot commitSnapshot(int snapshotId, String userName, String comment);
	
	public List<Snapshot> getSnapshots(int configId);
	
	public Snapshot getSnapshot(int snapshotId);
		
	public void deleteSnapshot(int snapshotId);

}
