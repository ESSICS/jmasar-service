package se.esss.ics.masar.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.node.Node;
import se.esss.ics.masar.persistence.dao.ConfigDAO;


public class ConfigJdbcDAO implements ConfigDAO {
	
	@Autowired
	private SimpleJdbcInsert configurationInsert;
	
	@Autowired
	private SimpleJdbcInsert configurationEntryInsert;
	
	@Autowired
	private SimpleJdbcInsert configurationEntryRelationInsert;
	
	@Autowired
	private SimpleJdbcInsert nodeInsert;
	
	@Autowired
	private SimpleJdbcInsert nodeClosureInsert;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	@Override
	public Node<Void> createNewFolder(Node<Void> node){
		
		int parentId = node.getParent().getId();
		
		Node<Void> parent = getParent(parentId, NodeType.FOLDER);
		
		node.setParent(parent);
		
		if(parent == null) {
			throw new IllegalArgumentException("No parent node found with id=" + parentId);
		}
		
		List<Node> existingChildNodes = getChildNodes(parentId);
		for(Node existingChildNode : existingChildNodes) {
			if(existingChildNode != null && node.getData() 
		}
		
		Date now = new Date();
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("name", node.getData());
		params.put("type", NodeType.FOLDER);
		params.put("created", now);
		params.put("last_modified", now);
		
		int newNodeId = nodeInsert.executeAndReturnKey(params).intValue();
		
		jdbcTemplate.update("insert into node_closure (ancestor, descendant, depth) "
				+ "select t.ancestor, " + newNodeId + ", t.depth + 1 " // For unknown reason newNodeId cannot be specified as parameter here (only Postgresql?)
				+ "from node_closure as t " 
				+ "where t.descendant = ? "
				+ "union all select ?, ?, 0",
				parentId, newNodeId, newNodeId);
		
		node.setId(newNodeId);
		
		return node;
	}
	
	@Override
	public <T> List<T> getChildNodes(int nodeId){
		return jdbcTemplate.query("select n.* from node as n join node_closure as nc on n.id=nc=descendant where "
				+ "(n.type='FOLDER' or n.TYPE='CONFIGURATION') and nc.ancestor=? and nc.depth=1",
				new Object[] {nodeId},
				new NodeRowMapper());
	}

	@Override
	public int saveConfig(Config config) {
		
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("name", config.getName());
		//params.put("created", config.getCreated());
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
		
		if(!list.isEmpty()) {
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
	
	private Node<Void> getParent(int parentId, NodeType parentNodeType){
		try {
			return jdbcTemplate.queryForObject("select * from node where id=? and type=?",
					new Object[] {parentId, parentNodeType.toString()},
						new NodeStringDataRowMapper());
		}
		catch (DataAccessException e) {
			return null;
		}
	}
	
	
}
