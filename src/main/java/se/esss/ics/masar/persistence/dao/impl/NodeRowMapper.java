package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.NodeType;

public class NodeRowMapper implements RowMapper<Node>{

	@Override
	public Node mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		Node node = new Node();
		
		node.setName(resultSet.getString("name"));
		node.setCreated(resultSet.getTimestamp("created"));
		node.setLastModified(resultSet.getTimestamp("last_modified"));
		node.setNodeType(NodeType.valueOf(resultSet.getString("type")));
		node.setId(resultSet.getInt("id"));
		
		return node;
	}
}
