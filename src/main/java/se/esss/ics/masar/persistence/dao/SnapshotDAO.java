package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;

public interface SnapshotDAO {

	/**
	 * Saves a snapshot to the database as a preliminary snapshot, i.e. without user id and comment.
	 * @param snapshot The {@link Snapshot} object to save together with the data read from the PVs
	 * @return The database id of the new snapshot.
	 */
	public int savePreliminarySnapshot(Snapshot snapshot);
	
	/**
	 * Get snapshots for the specified configuration id.
	 * @param configId The database id of the configuration see {@link Config#getId()}
	 * @return A list of {@link CommittedSnapshot} objects associated with the specified configuration id. 
	 * Snapshots that have not yet been committed (=saved with comment) are not included.
	 */
	public List<Snapshot> getSnapshots(int configId);
	
	/**
	 * Get a snapshot.
	 * @param snapshotId The database id of the snapshot, see {@link CommittedSnapshot#getId()}.
	 * @return A {@link CommittedSnapshot} object. <code>null</code> is returned if there is no snapshot corresponding to the specified
	 * snapshot id, or if the snapshot has not yet been committed (=saved with comment).
	 */
	public Snapshot getSnapshot(int snapshotId);
	
	/**
	 * "Saves" the snapshot by adding a user id and non-null comment. 
	 * @param snapshotId The database id of the snapshot, see {@link CommittedSnapshot#getId()}.
	 * @param userName The user identity.
	 * @param comment A non-null comment.
	 */
	public void commitSnapshot(int snapshotId, String userName, String comment);
	
	/**
	 * Deletes a snapshot and all associated data.
	 * @param snapshotId The database id of the snapshot, see {@link CommittedSnapshot#getId()}.
	 */
	public void deleteSnapshot(int snapshotId);
	
	public List<SnapshotPv> getSnapshotPvs(int snapshotId);
	
}
