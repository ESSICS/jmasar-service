package se.esss.ics.masar.persistence.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;

public class ConfigJdbcDAO implements ConfigDAO {

	@Autowired
	private SimpleJdbcInsert configurationInsert;

	@Autowired
	private SimpleJdbcInsert configurationEntryInsert;

	@Autowired
	private SimpleJdbcInsert configurationEntryRelationInsert;

	@Autowired
	private SimpleJdbcInsert snapshotInsert;

	@Autowired
	private SimpleJdbcInsert snapshotPvInsert;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SimpleJdbcInsert nodeInsert;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(ConfigJdbcDAO.class);

	@Transactional
	@Override
	public Node createNewFolder(final Node node) {
		return newNode(node);
	}

	private Node newNode(final Node node) {

		int parentId = node.getParent().getId();

		Node parent = getNodeInternal(parentId, false);

		if (parent == null) {
			throw new IllegalArgumentException("No parent node found with id=" + parentId);
		}

		// Check if any of the children of the parent node has same name and type

		List<Node> childNodes = getChildNodes(parent);
		for (Node childNode : childNodes) {
			if (childNode.getName().equals(node.getName()) && childNode.getNodeType().equals(node.getNodeType())) {
				throw new IllegalArgumentException(
						"Node of same name and type already exists in parent node " + node.getParent().getName() + ".");
			}
		}

		Date now = new Date();
		Map<String, Object> params = new HashMap<>(2);
		params.put("type", node.getNodeType().toString());
		params.put("created", now);
		params.put("last_modified", now);
		params.put("name", node.getName());

		int newNodeId = nodeInsert.executeAndReturnKey(params).intValue();

		jdbcTemplate.update(
				"insert into node_closure (ancestor, descendant, depth) " + "select t.ancestor, " + newNodeId
						+ ", t.depth + 1 " // For unknown reason newNodeId cannot be specified as parameter here (only
											// Postgresql?)
						+ "from node_closure as t " + "where t.descendant = ? " + "union all select ?, ?, 0",
				parentId, newNodeId, newNodeId);

		Node newNode = new Node();
		newNode.setId(newNodeId);
		newNode.setParent(parent);
		newNode.setLastModified(now);
		newNode.setName(node.getName());
		newNode.setNodeType(node.getNodeType());
		newNode.setCreated(now);

		return newNode;
	}

	@Override
	public List<Node> getChildNodes(Node node) {

		return jdbcTemplate.query("select n.* from node as n join node_closure as nc on n.id=nc.descendant where "
				+ "nc.ancestor=? and nc.depth=1", new Object[] { node.getId() }, new NodeRowMapper());
	}

	@Override
	public Config createNewConfiguration(Config config) {

		Node newNode = newNode(config);

		Map<String, Object> params = new HashMap<>(4);
		params.put("node_id", newNode.getId());
		params.put("description", config.getDescription());
		params.put("system", config.getSystem());
		params.put("active", config.isActive());

		configurationInsert.execute(params);

		if (config.getConfigPvList() != null) {
			for (ConfigPv configPv : config.getConfigPvList()) {
				saveConfigPv(newNode.getId(), configPv);
			}
		}

		return getConfig(newNode.getId());
	}

	private void saveConfigPv(int configId, ConfigPv configPv) {

		List<Integer> list = jdbcTemplate.queryForList("select id from config_pv where name=?",
				new Object[] { configPv.getPvName() }, Integer.class);

		int configPvId = 0;

		if (!list.isEmpty()) {
			configPvId = list.get(0);
		} else {
			Map<String, Object> params = new HashMap<>(4);
			params.put("name", configPv.getPvName());
			params.put("readonly", configPv.isReadonly());
			params.put("tags", configPv.getTags());
			params.put("groupName", configPv.getGroupname());

			configPvId = configurationEntryInsert.executeAndReturnKey(params).intValue();
		}

		Map<String, Object> params = new HashMap<>(2);
		params.put("config_id", configId);
		params.put("config_pv_id", configPvId);

		configurationEntryRelationInsert.execute(params);
	}

	@Override
	public Config getConfig(int configId) {
		Config config;
		try {
			config = jdbcTemplate.queryForObject(
					"select c.*, n.* from config as c " + "join node as n on c.node_id=n.id where c.node_id=?",
					new Object[] { configId }, new ConfigRowMapper());
		} catch (DataAccessException e) {
			return null;
		}

		config.setConfigPvList(getConfigPvs(configId));

		return config;
	}

	private List<ConfigPv> getConfigPvs(int configId) {
		return jdbcTemplate.query("select * from config_pv "
				+ "join config_pv_relation on config_pv.id=config_pv_relation.config_pv_id where config_pv_relation.config_id=?",
				new Object[] { configId }, new ConfigPvRowMapper());
	}

	private Node getNodeInternal(int nodeId, boolean includeChildNodes) {
		try {
			Node node = jdbcTemplate.queryForObject("select * from node where id=?", new Object[] { nodeId },
					new NodeRowMapper());
			if (node.getNodeType().equals(NodeType.FOLDER)) {
				if (includeChildNodes) {
					node.setChildren(getChildNodes(node));
				}
				return node;
			} else {
				return getConfig(nodeId);
			}
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Node getNode(int nodeId) {
		return getNodeInternal(nodeId, true);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Snapshot savePreliminarySnapshot(Snapshot snapshot) {

		Map<String, Object> snapshotParams = new HashMap<>();
		snapshotParams.put("config_id", snapshot.getConfigId());
		snapshotParams.put("created", new Date());

		int snapshotId = snapshotInsert.executeAndReturnKey(snapshotParams).intValue();

		Map<String, Object> params = new HashMap<>(6);
		params.put("snapshot_id", snapshotId);

		for (SnapshotPv snapshotPv : snapshot.getSnapshotPvList()) {
			params.put("config_pv_id", snapshotPv.getConfigPv().getId());
			params.put("fetch_status", snapshotPv.isFetchStatus());
			if (snapshotPv.isFetchStatus()) {
				params.put("dtype", snapshotPv.getDtype());
				params.put("severity", snapshotPv.getSeverity());
				params.put("status", snapshotPv.getStatus());
				params.put("time", snapshotPv.getTime());
				params.put("timens", snapshotPv.getTimens());
				params.put("clazz", snapshotPv.getValue().getClass().getCanonicalName());
				try {
					params.put("value", objectMapper.writeValueAsString(snapshotPv.getValue()));
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				}
			}

			snapshotPvInsert.execute(params);
		}

		snapshot.setId(snapshotId);

		return snapshot;
	}

}
