package se.esss.ics.masar.model.snapshot;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Snapshot {

	private int id;
	private int configId;
	private Date created;
	private String userName;
	private int username_id;
	private String comment;
	private boolean approve;
	
	private List<SnapshotPv> snapshotPvList;
}
