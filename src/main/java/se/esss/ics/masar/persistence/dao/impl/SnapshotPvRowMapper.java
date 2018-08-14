package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.SnapshotPv;

@SuppressWarnings("rawtypes")
public class SnapshotPvRowMapper implements RowMapper<SnapshotPv<?>> {
	
	private ObjectMapper objectMapper;
	
	public SnapshotPvRowMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public SnapshotPv<?> mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		ConfigPv configPv = new ConfigPvRowMapper().mapRow(resultSet, rowIndex);
		
		return SnapshotPv.builder()
				.dtype(resultSet.getInt("dtype"))
				.fetchStatus(resultSet.getBoolean("fetch_status"))
				.snapshotId(resultSet.getInt("snapshot_id"))
				.severity(resultSet.getInt("severity"))
				.status(resultSet.getInt("status"))
				.time(resultSet.getLong("time"))
				.timens(resultSet.getInt("timens"))
				.configPv(configPv)
				.value(getTypedValue(resultSet.getString("value"), resultSet.getString("clazz")))
				.build();
	
	}
	
	@SuppressWarnings("unchecked")
	protected Object getTypedValue(String valueAsString, String className) {
		
		if(className == null) {
			LoggerFactory.getLogger(SnapshotPvRowMapper.class).error("Not attempting to read a value of null class name");
			return null;
		}
		
		try {
			Class clazz = Class.forName(className);
			
			return objectMapper.readValue(valueAsString, clazz);
			
		} catch (Exception e) {
			LoggerFactory.getLogger(SnapshotPvRowMapper.class).error(e.getMessage());
			return null;
		}
	}
}
