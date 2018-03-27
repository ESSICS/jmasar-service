package se.esss.ics.masar.model.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.esss.ics.masar.model.NodeType;

public class Node {

	private List<Node> children = new ArrayList<Node>();
	private Node parent;
	private int id;
	private String name;
	private Date created;
	private Date lastModified;
	private NodeType nodeType = NodeType.FOLDER;
	
	public Node() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}
}
