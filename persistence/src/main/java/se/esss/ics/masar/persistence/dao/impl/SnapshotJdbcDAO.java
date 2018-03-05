package se.esss.ics.masar.persistence.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;

public class SnapshotJdbcDAO implements SnapshotDAO {

	@Autowired
	private SimpleJdbcInsert snapshotInsert;
	
	@Autowired
	private SimpleJdbcInsert snapshotPvInsert;
	
	@Autowired 
	private SimpleJdbcInsert userNameInsert;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ObjectMapper objectMapper; 
	
	private static final int NO_USER = -1;
	
	
	@Override
	public int savePreliminarySnapshot(Snapshot snapshot) {

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("config_id", snapshot.getConfigId());
		params.put("created", snapshot.getCreated());
		
		int snapshotId = snapshotInsert.executeAndReturnKey(params).intValue();
		
		params = new HashMap<String, Object>(8);
		params.put("snapshot_id", snapshotId);
		for(SnapshotPv snapshotPv : snapshot.getSnapshotPvList()) {
			params.put("dtype", snapshotPv.getDtype());
			params.put("severity", snapshotPv.getSeverity());
			params.put("status", snapshotPv.getStatus());
			params.put("time", snapshotPv.getTime());
			params.put("timens", snapshotPv.getTimens());
			params.put("clazz", snapshotPv.getValue().getClass().getCanonicalName());
			try {
				params.put("value", objectMapper.writeValueAsString(snapshotPv.getValue()));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params.put("fetch_status", snapshotPv.isFetchStatus());
			
			snapshotPvInsert.execute(params);
		}
	
		return snapshotId;
	}
	
	@Override
	public void commitSnapshot(int snapshotId, String userName, String comment) {
		
		int userId = getUserNameId(userName);
		if(userId == NO_USER) {
			userId = userNameInsert.executeAndReturnKey(Collections.singletonMap("name", userName)).intValue();
		}
		
		jdbcTemplate.update("update snapshot set username_id=?, comment=? where id=?",
				new Object[] {userId, comment, snapshotId});
	}
	
	private int getUserNameId(String userName) {
		try {
			return jdbcTemplate.queryForObject("select id from userName where name=?",
					new Object[] {userName},
					Integer.class);
		} catch (DataAccessException e) {
			return NO_USER;
		}
	}
	
	@Override
	public List<Snapshot> getSnapshots(int configId){
		return jdbcTemplate.query("select snapshot.id, config_id, created, comment, approve, name from snapshot join " +
				"username on snapshot.username_id=username.id where snapshot.config_id=?", 
				new Object[] {configId}, 
				new SnapshotRowMapper());
	}
	
	@Override
	public Snapshot getSnapshot(int snapshotId) {
		return getSnapshot(snapshotId, true);
	}
	
	private Snapshot getSnapshot(int snapshotId, boolean includeSnapshotPvs) {
		
		Snapshot snapshot;
		try {
			snapshot = jdbcTemplate.queryForObject("select * from snapshot where id=?", 
					new Object[] {snapshotId}, 
					new SnapshotRowMapper());
		} catch (DataAccessException e) {
			// No snapshot corresponding to snapshotId found.
			return null;
		}
		
		if(snapshot.getUsername_id() > 0) {
			String userName = jdbcTemplate.queryForObject("select name from username where id=?", 
					new Object[] {snapshot.getUsername_id()},
					String.class);
			snapshot.setUserName(userName);
		}
		
		if(includeSnapshotPvs) {
			snapshot.setSnapshotPvList(getSnapshotPvs(snapshotId));
		}
		
		return snapshot;
		
	}
	
	@Override
	public void deleteSnapshot(int snapshotId) {
		jdbcTemplate.update("delete from snapshot where id=?", new Object[] {snapshotId});
	}
	
	private List<SnapshotPv> getSnapshotPvs(int snapshotId) {
		
		return jdbcTemplate.query("select * from snapshot_pv where snapshot_id=?",  
				new Object[] {snapshotId}, 
				new SnapshotPvRowMapper(objectMapper));
	}

}
