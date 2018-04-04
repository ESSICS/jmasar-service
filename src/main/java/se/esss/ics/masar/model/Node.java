package se.esss.ics.masar.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {

	private List<Node> children;
	private Node parent;
	private int id;
	private String name;
	private Date created;
	private Date lastModified;
	
	private NodeType nodeType = NodeType.FOLDER;

	@Override
	public boolean equals(Object other) {
		if(other instanceof Node) {
			Node node = (Node)other;
			return id == node.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
