package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.Config;

public class ConfigRowMapper implements RowMapper<Config> {

	@Override
	public Config mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
		Config config = Config.builder()
				.system(resultSet.getString("system"))
				.active(resultSet.getBoolean("active"))
				.description(resultSet.getString("description"))
				.build();
		
		config.setId(resultSet.getInt("id"));
		config.setName(resultSet.getString("name"));
		config.setCreated(resultSet.getTimestamp("created"));
		config.setLastModified(resultSet.getTimestamp("last_modified"));
		
		return config;
	}
}
