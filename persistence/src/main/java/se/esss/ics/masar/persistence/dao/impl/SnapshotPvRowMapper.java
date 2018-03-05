package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.snapshot.SnapshotPv;

public class SnapshotPvRowMapper implements RowMapper<SnapshotPv> {
	
	private ObjectMapper objectMapper;
	
	public SnapshotPvRowMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public SnapshotPv mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		return SnapshotPv.builder()
				.dtype(resultSet.getInt("dtype"))
				.fetchStatus(resultSet.getBoolean("fetch_status"))
				.id(resultSet.getInt("id"))
				.severity(resultSet.getInt("severity"))
				.status(resultSet.getInt("status"))
				.time(resultSet.getLong("time"))
				.timens(resultSet.getInt("timens"))
				.value(getTypedValue(resultSet.getString("value"), resultSet.getString("clazz")))
				.build();
	
	}
	
	private Object getTypedValue(String valueAsString, String className) {
		try {
			Class clazz = Class.forName(className);
			
			return objectMapper.readValue(valueAsString, clazz);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
