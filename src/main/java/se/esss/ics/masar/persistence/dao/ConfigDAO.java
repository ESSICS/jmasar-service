package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.node.Node;
import se.esss.ics.masar.model.snapshot.Snapshot;


public interface ConfigDAO {
	
	public Node createNewFolder(Node node);
	
	public Node createNewConfiguration(Config config);
	
	public List<Config> getConfigs();
	
	public Config getConfig(int configId);
	
	public List<ConfigPv> getConfigPvs(int configId);
	
	public List<Node> getChildNodes(Node node);
	
	/**
	 * Saves a snapshot to the database as a preliminary snapshot, i.e. without user id and comment.
	 * @param snapshot The {@link Snapshot} object to save together with the data read from the PVs
	 * @return The database id of the new snapshot.
	 */
	public Snapshot savePreliminarySnapshot(Snapshot snapshot);
	
	public Node getNode(int nodeId);

		
}
