/** 
 * Copyright (C) ${year} European Spallation Source ERIC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.Snapshot;

public interface SnapshotDAO {

		
	/**
	 * Get snapshots for the specified configuration id.
	 * @param configId The database id of the configuration see {@link Config#getId()}
	 * @return A list of {@link Snapshot} objects associated with the specified configuration id. 
	 * Snapshots that have not yet been committed (=saved with comment) are not included.
	 */
	public List<Snapshot> getSnapshots(int configId);
	
	/**
	 * Get a snapshot.
	 * @param snapshotId The database id of the snapshot, see {@link Snapshot#getId()}.
	 * @param commitedOnly If <code>true</code>, the snapshot must be a committed one.
	 * @return A {@link Snapshot} object. <code>null</code> is returned if there is no snapshot corresponding to the specified
	 * snapshot id, or if <code>committedOnly=true</code> and for a snapshot with matching id that has not been committed.
	 */
	public Snapshot getSnapshot(int snapshotId, boolean commitedOnly);
	
	/**
	 * "Saves" the snapshot by adding a user id and non-null comment. 
	 * @param snapshotId The database id of the snapshot, see {@link Snapshot#getId()}.
	 * @param userName The user identity.
	 * @param comment A non-null comment.
	 */
	public void commitSnapshot(int snapshotId, String userName, String comment);
	
	/**
	 * Deletes a snapshot and all associated data.
	 * @param snapshotId The database id of the snapshot, see {@link Snapshot#getId()}.
	 */
	public void deleteSnapshot(int snapshotId);
	
	
}
