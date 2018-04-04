package se.esss.ics.masar.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Snapshot{
	
	private Date created;
	private int id;
	private int configId;
	private boolean approve;
	private String userName;
	private int usernameId;
	private String comment;
	
	private List<SnapshotPv<?>> snapshotPvList;
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Snapshot) {
			Snapshot snapshot = (Snapshot)other;
			return id == snapshot.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
}
