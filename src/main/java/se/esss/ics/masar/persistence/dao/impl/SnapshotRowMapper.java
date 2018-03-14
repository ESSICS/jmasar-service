package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.snapshot.Snapshot;

public class SnapshotRowMapper implements RowMapper<Snapshot> {

	@Override
	public Snapshot mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
	
		return Snapshot.builder()
				.approve(resultSet.getBoolean("approve"))
				.comment(resultSet.getString("comment"))
				.configId(resultSet.getInt("config_id"))
				.username_id(resultSet.getInt("username_id"))
				.userName(resultSet.getString("name"))
				.created(new java.util.Date(resultSet.getTimestamp("created").getTime()))
				.id(resultSet.getInt("id"))
				.build();
	}
}
