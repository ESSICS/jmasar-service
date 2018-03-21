package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.node.Node;

public class NodeStringDataRowMapper implements RowMapper<Node<Void>>{

	@Override
	public Node<Void> mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		Node<Void> node = new Node();
		node.setName(resultSet.getString("name"));
		node.setCreated(resultSet.getDate("created"));
		node.setLastModified(resultSet.getDate("last_modified"));
		node.setNodeType(NodeType.valueOf(resultSet.getString("type")));
		node.setId(resultSet.getInt("id"));
		
		return node;
	}
}
