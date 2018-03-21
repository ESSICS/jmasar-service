package se.esss.ics.masar.model.node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.esss.ics.masar.model.NodeType;

public class Node<T> {
	
	private List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent;
    private T data = null;
    private int id;
    private String name;
    private Date created;
    private Date lastModified;
    private NodeType nodeType = NodeType.FOLDER;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node() {
    }
    
    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
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

	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }
    
    public Node<T> getParent(){
    		return parent;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void removeParent() {
        this.parent = null;
    }
}
