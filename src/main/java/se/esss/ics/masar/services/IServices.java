package se.esss.ics.masar.services;

import java.util.List;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Snapshot;

public interface IServices {
	
	public Folder createFolder(Folder folder);
	
	public Folder getFolder(int nodeId);
		
	public Config createNewConfiguration(Config configuration);
	
	public Config getConfiguration(int nodeId);
		
	public void deleteConfiguration(int nodeId);
		
	public Snapshot takeSnapshot(int configId);
	
	public Snapshot commitSnapshot(int snapshotId, String userName, String comment);
	
	public List<Snapshot> getSnapshots(int configId);
	
	public Snapshot getSnapshot(int snapshotId);
		
	public void deleteSnapshot(int snapshotId);
	
	public Folder moveNode(int nodeId, int targetNodeId);
	
	public void deleteFolder(int nodeId);

}
