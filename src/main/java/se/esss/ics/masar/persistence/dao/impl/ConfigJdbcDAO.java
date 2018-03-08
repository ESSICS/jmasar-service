package se.esss.ics.masar.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;


public class ConfigJdbcDAO implements ConfigDAO {
	
	@Autowired
	private SimpleJdbcInsert configurationInsert;
	
	@Autowired
	private SimpleJdbcInsert configurationEntryInsert;
	
	@Autowired
	private SimpleJdbcInsert configurationEntryRelationInsert;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int saveConfig(Config config) {
		
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("name", config.getName());
		params.put("created", config.getCreated());
		params.put("description", config.getDescription());
		params.put("system", config.getSystem());
		params.put("active", config.isActive());
	
		int configId = configurationInsert.executeAndReturnKey(params).intValue();
		
		for(ConfigPv configPv : config.getConfigPvList()) {
			saveConfigPv(configId, configPv);
		}
		
		return configId;
	}

	private void saveConfigPv(int configId, ConfigPv configPv) {
		
		List<Integer> list = jdbcTemplate.queryForList("select id from config_pv where name=?", 
				new Object[] {configPv.getPvName()},
				Integer.class);
		
		int configPvId = 0;
		
		if(list != null && !list.isEmpty()) {
			configPvId = list.get(0);
		}
		else {
			Map<String, Object> params = new HashMap<String, Object>(4);
			params.put("name", configPv.getPvName());
			params.put("readonly", configPv.isReadonly());
			params.put("tags", configPv.getTags());
			params.put("groupName", configPv.getGroupname());
			
			configPvId = configurationEntryInsert.executeAndReturnKey(params).intValue();
		}
		
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("config_id", configId);
		params.put("config_pv_id", configPvId);
		
		configurationEntryRelationInsert.execute(params);
	}
	
	@Override
	public Config getConfig(int configId) {
		return getConfig(configId, true);
	}
	
	private Config getConfig(int configId, boolean includePvList) {
		Config config;
		try {
			config = jdbcTemplate.queryForObject("select * from config where id=?",  new Object[] {configId}, new ConfigRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
		
		if(includePvList) {
			config.setConfigPvList(getConfigPvs(configId));
		}
		
		return config;
	}
	
	@Override
	public List<Config> getConfigs(){
		List<Integer> configIds = jdbcTemplate.queryForList("select id from config", Integer.class);
		List<Config> configs = new ArrayList<>();
		for(Integer configId : configIds) {
			configs.add(getConfig(configId, false));
		}
		
		return configs;
	}
	
	@Override
	public List<ConfigPv> getConfigPvs(int configId){
		return jdbcTemplate.query("select * from config_pv " +
				"join config_pv_relation on config_pv.id=config_pv_relation.config_pv_id where config_pv_relation.config_id=?",  
				new Object[] {configId}, new ConfigPvRowMapper());
	}
}
