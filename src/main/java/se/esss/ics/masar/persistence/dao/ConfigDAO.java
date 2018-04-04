package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;


public interface ConfigDAO {
	
	public Node createNewFolder(Node node);
	
	public Config createNewConfiguration(Config config);
	
	public Config getConfig(int configId);
	
	public List<Node> getChildNodes(Node node);
	
	/**
	 * Saves a snapshot to the database as a preliminary snapshot, i.e. without user id and comment.
	 * @param snapshot The {@link Snapshot} object to save together with the data read from the PVs
	 * @return The database id of the new snapshot.
	 */
	public Snapshot savePreliminarySnapshot(Snapshot snapshot);
	
	/**
	 * Retrieves a {@link Node} identified by the database id.
	 * @param nodeId
	 * @return A {@link Node} object including child nodes. If the id is a configuration node, a {@link Config}
	 * object is returned instead, including the list of {@link ConfigPv}s.
	 */
	public Node getNode(int nodeId);

		
}
