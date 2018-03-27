package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.node.Node;

public class NodeRowMapper implements RowMapper<Node>{

	@Override
	public Node mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		NodeType nodeType = NodeType.valueOf(resultSet.getString("type"));
		
		Node node;
		
		switch(nodeType) {
			case FOLDER:
				node = new Node();
				break;
			case CONFIGURATION:
				node = new Config();
				break;
			default:
				return null;
		}
		node.setName(resultSet.getString("name"));
		node.setCreated(resultSet.getDate("created"));
		node.setLastModified(resultSet.getDate("last_modified"));
		node.setNodeType(NodeType.valueOf(resultSet.getString("type")));
		node.setId(resultSet.getInt("id"));
		
		return node;
	}
}
