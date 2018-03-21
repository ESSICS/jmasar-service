package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.snapshot.Snapshot;

public class SnapshotRowMapper implements RowMapper<Snapshot> {

	@Override
	public Snapshot mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
	
		return null;
	}
}
