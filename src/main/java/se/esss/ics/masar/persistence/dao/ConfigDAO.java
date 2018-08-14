package se.esss.ics.masar.persistence.dao;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;

public interface ConfigDAO {
	
	public Folder createFolder(Folder node);

	public Config createConfiguration(Config config);

	/**
	 * Retrieves a folder identified by the node id. 
	 * @param nodeId If there is no node corresponding to the node id, an {@link IllegalArgumentException} is thrown.
	 * @return A {@link Folder} object.
	 */
	public Folder getFolder(int nodeId);
	
	/**
	 * Retrieves a configuration identified by the node id. 
	 * @param nodeId If there is no node corresponding to the node id, an {@link IllegalArgumentException} is thrown.
	 * @return A {@link Config} object.
	 */
	public Config getConfiguration(int nodeId);
	
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
	 * Deletes a {@link Node}, folder or configuration. If the node is a folder, 
	 * the entire sub-tree of the folder is deleted, including the snapshots 
	 * associated with configurations in the sub-tree.
	 * 
	 * @param nodeId
	 *            The node id node to delete.
	 */
	public void deleteNode(int nodeId);

	public Folder moveNode(int nodeId, int targetNodeId);

	/**
	 * Updates an existing configuration, e.g. changes its name or list of PVs.
	 * @param config The updated configuration data
	 * @return The updated configuration object
	 */
	public Config updateConfiguration(Config config);
	
	/**
	 * Renames an existing node.
	 * @param nodeId The node id of the node subject to change. The root folder's name cannot be changed.
	 * @param name The new name of the node. The name and node type must be unique in the parent folder.
	 * @return The updated {@link Node} object.
	 */
	public Node renameNode(int nodeId, String name);

}
