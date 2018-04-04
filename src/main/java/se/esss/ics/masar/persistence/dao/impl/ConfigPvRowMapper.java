package se.esss.ics.masar.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import se.esss.ics.masar.model.ConfigPv;


public class ConfigPvRowMapper implements RowMapper<ConfigPv> {
	
	@Override
	public ConfigPv mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
		return
				ConfigPv.builder()
				.id(resultSet.getInt("id"))
				.groupname(resultSet.getString("groupName"))
				.pvName(resultSet.getString("name"))
				.readonly(resultSet.getBoolean("readonly"))
				.tags(resultSet.getString("tags"))
				.build();
	}
}
