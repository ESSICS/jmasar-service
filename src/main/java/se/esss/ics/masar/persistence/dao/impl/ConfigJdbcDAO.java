package se.esss.ics.masar.persistence.dao.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.services.exception.NodeNotFoundException;

public class ConfigJdbcDAO implements ConfigDAO {

	@Autowired
	private SnapshotDAO snapshotDAO;

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
	public Folder createFolder(final Folder folder) {

		Node newNode = newNode(folder);

		return getFolder(newNode.getId());
	}

	@Transactional
	@Override
	public Folder getFolder(int nodeId) {
		Node node = getNode(nodeId);
		if (node instanceof Folder) {
			return (Folder) node;
		} else {
			throw new NodeNotFoundException(String.format("Node id=%d is not a folder node", nodeId));
		}
	}

	@Transactional
	@Override
	public Config getConfiguration(int nodeId) {

		Node node = getNode(nodeId);
		if (node instanceof Config) {
			return (Config) node;
		} else {
			throw new NodeNotFoundException(String.format("Node id=%d is not a configuration node", nodeId));
		}

	}

	private Node getParentNode(int nodeId) {

		try {
			int parentNodeId = jdbcTemplate.queryForObject(
					"select ancestor from node_closure where descendant=? and depth=1", new Object[] { nodeId },
					Integer.class);
			return getNodeInternal(parentNodeId);
		} catch (DataAccessException e) {
			return null;
		}
	}

	private Node newNode(final Node node) {

		if (node.getParent() == null) {
			throw new IllegalArgumentException("Cannot create a node without a parent.");
		}

		Node parent = getNodeInternal(node.getParent().getId());

		// The node to be created cannot have same name and type as any of the parent's
		// child nodes

		List<Node> childNodes = getChildNodes(parent.getId());
		if (doesNameClash(node, childNodes)) {
			throw new IllegalArgumentException("Node of same name and type already exists in parent node.");
		}
	
		Timestamp now = Timestamp.from(Instant.now());

		Map<String, Object> params = new HashMap<>(2);
		params.put("type", node.getNodeType().toString());
		params.put("created", now);
		params.put("last_modified", now);
		params.put("name", node.getName());

		int newNodeId = nodeInsert.executeAndReturnKey(params).intValue();

		jdbcTemplate.update(
				"insert into node_closure (ancestor, descendant, depth) " + "select t.ancestor, " + newNodeId
						+ ", t.depth + 1  from node_closure as t where t.descendant = ? union all select ?, ?, 0",
				parent.getId(), newNodeId, newNodeId);

		// Update the last modified date of the parent folder
		jdbcTemplate.update("update node set last_modified=? where id=?", Timestamp.from(Instant.now()), parent.getId());

		return getNodeInternal(newNodeId);
	}

	private Node getNode(int nodeId) {

		Node node = getNodeInternal(nodeId);

		if (node.getNodeType().equals(NodeType.CONFIGURATION)) {

			Config config = jdbcTemplate.queryForObject(
					"select n.*, c.* from node as n join config as c on n.id=c.node_id where" + " n.id=? and n.type=?",
					new Object[] { nodeId, NodeType.CONFIGURATION.toString() }, new ConfigRowMapper());

			config.setConfigPvList(getConfigPvs(config.getId()));
			config.setParent(getParentNode(config.getId()));
			return config;
		} else {
			return Folder.builder().created(node.getCreated()).lastModified(node.getLastModified()).id(node.getId())
					.childNodes(getChildNodes(node.getId())).parent(getParentNode(nodeId)).name(node.getName()).build();
		}
	}

	private List<Node> getChildNodes(int nodeId) {

		return jdbcTemplate.query("select n.* from node as n join node_closure as nc on n.id=nc.descendant where "
				+ "nc.ancestor=? and nc.depth=1", new Object[] { nodeId }, new NodeRowMapper());
	}

	@Override
	@Transactional
	public Config createConfiguration(Config config) {

		Node newNode = newNode(config);

		Map<String, Object> params = new HashMap<>(4);
		params.put("node_id", newNode.getId());
		params.put("description", config.getDescription());
		params.put("_system", config.getSystem());
		params.put("active", config.isActive());
		params.put("last_modified", Timestamp.from(Instant.now()));

		configurationInsert.execute(params);

		if (config.getConfigPvList() != null) {
			for (ConfigPv configPv : config.getConfigPvList()) {
				saveConfigPv(newNode.getId(), configPv);
			}
		}

		return getConfiguration(newNode.getId());
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

	private List<ConfigPv> getConfigPvs(int configId) {
		return jdbcTemplate.query("select * from config_pv "
				+ "join config_pv_relation on config_pv.id=config_pv_relation.config_pv_id where config_pv_relation.config_id=?",
				new Object[] { configId }, new ConfigPvRowMapper());
	}

	private Node getNodeInternal(int nodeId) {
		try {
			return jdbcTemplate.queryForObject("select * from node where id=?", new Object[] { nodeId },
					new NodeRowMapper());
		} catch (DataAccessException e) {
			throw new NodeNotFoundException(
					String.format("Unable to retrieve node id=%d not found, cause: %s", nodeId, e.getMessage()));
		}
	}

	protected void deleteConfiguration(int nodeId) {

		List<Integer> configPvIds = jdbcTemplate.queryForList(
				"select config_pv_id from config_pv_relation where config_id=?", new Object[] { nodeId },
				Integer.class);

		jdbcTemplate.update("delete from node where id=? and type=?", nodeId, NodeType.CONFIGURATION.toString());

		deleteOrphanedPVs(configPvIds);
	}

	@Override
	public void deleteNode(int nodeId) {

		// Root node may not be deleted
		if (nodeId == Node.ROOT_NODE_ID) {
			return;
		}
		Node nodeToDelete = getNode(nodeId);
		Node parentNode = nodeToDelete.getParent();
		if (nodeToDelete instanceof Config) {
			deleteConfiguration(nodeId);
		} else {
			Folder folderToDelete = (Folder) nodeToDelete;
			for (Node node : folderToDelete.getChildNodes()) {
				deleteNode(node.getId());
			}
		}
		jdbcTemplate.update("delete from node where id=?", nodeId);

		// Update last modified date of the parent node
		jdbcTemplate.update("update node set last_modified=? where id=?", Timestamp.from(Instant.now()), parentNode.getId());
	}

	private void deleteOrphanedPVs(Collection<Integer> pvList) {
		for (Integer pvId : pvList) {
			int count = jdbcTemplate.queryForObject("select count(*) from config_pv_relation where config_pv_id=?",
					new Object[] { pvId }, Integer.class);

			if (count == 0) {
				jdbcTemplate.update("delete from config_pv where id=?", pvId);
			}
		}
	}

	@Override
	public Snapshot savePreliminarySnapshot(Snapshot snapshot) {

		Map<String, Object> snapshotParams = new HashMap<>();
		snapshotParams.put("config_id", snapshot.getConfigId());
		snapshotParams.put("created", Timestamp.from(Instant.now()));

		int snapshotId = snapshotInsert.executeAndReturnKey(snapshotParams).intValue();

		Map<String, Object> params = new HashMap<>(6);
		params.put("snapshot_id", snapshotId);

		for (SnapshotPv<?> snapshotPv : snapshot.getSnapshotPvList()) {
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

		return snapshotDAO.getSnapshot(snapshotId, false);

	}

	@Override
	@Transactional
	public Folder moveNode(int nodeId, int targetNodeId) {

		Node sourceNode = getNode(nodeId);

		int parentNodeId = sourceNode.getParent().getId();

		Folder targetNode = getFolder(targetNodeId);

		if (doesNameClash(sourceNode, targetNode.getChildNodes())) {
			throw new IllegalArgumentException("Node of same name and type already exists in target node.");
		}

		jdbcTemplate.update("delete from node_closure where "
				+ "descendant in (select descendant from node_closure where ancestor=?) "
				+ "and ancestor in (select ancestor from node_closure where descendant=? and ancestor != descendant)",
				nodeId, nodeId);

		jdbcTemplate.update("insert into node_closure (ancestor, descendant, depth) "
				+ "select supertree.ancestor, subtree.descendant, supertree.depth + subtree.depth + 1 AS depth "
				+ "from node_closure as supertree " + "cross join node_closure as subtree "
				+ "where supertree.descendant=? and subtree.ancestor=?", targetNodeId, nodeId);

		// Update the last modified date of the source and target folder.
		jdbcTemplate.update("update node set last_modified=? where id=? or id=?", Timestamp.from(Instant.now()), targetNodeId,
				parentNodeId);

		return getFolder(targetNodeId);
	}

	protected boolean doesNameClash(Node nodeToCheck, List<Node> existingNodes) {
		for (Node node : existingNodes) {
			if (node.getName().equals(nodeToCheck.getName()) && node.getNodeType().equals(nodeToCheck.getNodeType())) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public Config updateConfiguration(Config config) {

		Config existingConfig = (Config) getNode(config.getId());

		Collection<ConfigPv> pvsToRemove = CollectionUtils.removeAll(existingConfig.getConfigPvList(),
				config.getConfigPvList());
		CollectionUtils.removeAll(config.getConfigPvList(), existingConfig.getConfigPvList());

		Collection<Integer> pvIdsToRemove = CollectionUtils.collect(pvsToRemove, ConfigPv::getId);

		deleteOrphanedPVs(pvIdsToRemove);

		jdbcTemplate.update("update config set description=?, _system=? where node_id=?", config.getDescription(),
				config.getSystem(), config.getId());
		jdbcTemplate.update("update node set name=? where id=?", config.getName(), config.getId());

		return getConfiguration(config.getId());
	}

	@Override
	@Transactional
	public Node renameNode(int nodeId, String name) {

		if (nodeId == Node.ROOT_NODE_ID) {
			throw new IllegalArgumentException("Cannot change name of root folder");
		}

		Node nodeToChange = getNode(nodeId);

		Folder parentFolder = getFolder(nodeToChange.getParent().getId());

		// Create a node object used to check name clash
		Node tmp = new Node();
		tmp.setName(name);
		tmp.setNodeType(nodeToChange.getNodeType());

		if (doesNameClash(tmp, parentFolder.getChildNodes())) {
			throw new IllegalArgumentException(
					"Cannot change name of node as an existing node with same name and type exists.");
		}

		jdbcTemplate.update("update node set name=? where id=?", name, nodeId);

		return getNode(nodeId);
	}

}