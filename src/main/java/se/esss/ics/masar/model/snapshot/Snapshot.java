package se.esss.ics.masar.model.snapshot;

import java.util.List;

import se.esss.ics.masar.model.node.NodeData;

public class Snapshot extends NodeData{
	
	private boolean approve;
	private String userName;
	private int username_id;
	private String comment;
	
	@SuppressWarnings("rawtypes")
	private List<SnapshotPv> snapshotPvList;

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUsername_id() {
		return username_id;
	}

	public void setUsername_id(int username_id) {
		this.username_id = username_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<SnapshotPv> getSnapshotPvList() {
		return snapshotPvList;
	}

	public void setSnapshotPvList(List<SnapshotPv> snapshotPvList) {
		this.snapshotPvList = snapshotPvList;
	}
	
	
}
