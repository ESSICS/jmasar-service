package se.esss.ics.masar.persistence.dao.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
		List<Snapshot> snapshots = jdbcTemplate.query("select snapshot.id, config_id, username_id, created, comment, approve, name from snapshot join " +
				"username on snapshot.username_id=username.id where snapshot.config_id=?", 
				new Object[] {configId}, 
				new SnapshotRowMapper());
		
		return snapshots;
	}
	
	@Override
	public List<SnapshotPv<?>> getSnapshotPvValues(int snapshotId){
		return jdbcTemplate.query("select * from snapshot_pv where snapshot_id=?",
				new Object[] {snapshotId},
				new SnapshotPvRowMapper(objectMapper));
	}
	
	
	@Override
	public Snapshot getSnapshot(int snapshotId) {
		
		Snapshot snapshot;
		try {
			snapshot = jdbcTemplate.queryForObject("select snapshot.id, config_id, username_id, created, comment, approve, name from snapshot join " +
					"username on snapshot.username_id=username.id where snapshot.id=?", 
					new Object[] {snapshotId}, 
					new SnapshotRowMapper());
		} catch (DataAccessException e) {
			// No committed snapshot corresponding to snapshotId found
			return null;
		}
		
		snapshot.setSnapshotPvList(getSnapshotPvValues(snapshotId));
		
		return snapshot;
		
	}
	
	@Override
	public void deleteSnapshot(int snapshotId) {
		jdbcTemplate.update("delete from snapshot where id=?", new Object[] {snapshotId});
	}
}
