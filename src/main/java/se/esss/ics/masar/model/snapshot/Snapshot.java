package se.esss.ics.masar.model.snapshot;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
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
}
