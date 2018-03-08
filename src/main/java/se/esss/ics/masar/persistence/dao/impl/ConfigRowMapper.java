package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.config.Config;

public class ConfigRowMapper implements RowMapper<Config> {

	@Override
	public Config mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		
			
		return Config.builder()
				.id(resultSet.getInt("id"))
				.active(resultSet.getBoolean("active"))
				.name(resultSet.getString("name"))
				.created(new java.util.Date(resultSet.getTimestamp("created").getTime()))
				.description(resultSet.getString("description"))
				.system(resultSet.getString("system"))
				.build();
	}
}
