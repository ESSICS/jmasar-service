package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.Snapshot;

public class SnapshotRowMapper implements RowMapper<Snapshot> {

	@Override
	public Snapshot mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
			
		return Snapshot.builder()
				.approve(resultSet.getBoolean("approve"))
				.configId(resultSet.getInt("config_id"))
				.created(resultSet.getTimestamp("created"))
				.id(resultSet.getInt("id"))
				.userName(resultSet.getString("name"))
				.comment(resultSet.getString("comment"))
				.build();
	}
}
