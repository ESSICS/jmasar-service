/** 
 * Copyright (C) ${year} European Spallation Source ERIC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
