package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;

public interface ConfigDAO {

	public Folder createFolder(Folder node);

	/**
	 * @param nodeId
	 *            The database id associated with the folder.
	 * @return The {@link Folder} corresponding to the node id, including all its
	 *         child nodes. <code>null</code> is returned if there is no folder
	 *         corresponding to the specified id, or if the specified id is
	 *         associated with a {@link Config}.
	 */
	public Folder getFolder(int nodeId);

	public Config createConfiguration(Config config);

	public Config getConfiguration(int nodeId);

	public List<Node> getChildNodes(int nodeId);

	/**
	 * Saves a snapshot to the database as a preliminary snapshot, i.e. without user
	 * id and comment.
	 * 
	 * @param snapshot
	 *            The {@link Snapshot} object to save together with the data read
	 *            from the PVs
	 * @return The database id of the new snapshot.
	 */
	public Snapshot savePreliminarySnapshot(Snapshot snapshot);

	/**
	 * Deletes a {@link Config}. Any snapshots - whether preliminary or committed -
	 * associated with this configuration will also be deleted. PVs
	 * ({@link ConfigPv}) referenced with this configuration will be deleted unless
	 * referenced by any other configuration.
	 * 
	 * @param nodeId
	 *            The node id of the configuration to delete.
	 */
	public void deleteConfiguration(int nodeId);

	public Folder moveNode(int nodeId, int targetNodeId);

	public void deleteFolder(int nodeId);

}
