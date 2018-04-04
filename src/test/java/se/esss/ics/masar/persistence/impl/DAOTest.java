package se.esss.ics.masar.persistence.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.config.PersistenceConfiguration;
import se.esss.ics.masar.persistence.config.PersistenceTestConfig;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableConfigurationProperties
@ContextHierarchy({ @ContextConfiguration(classes = { PersistenceConfiguration.class, PersistenceTestConfig.class }) })
@TestPropertySource(properties = {"dbengine = h2"})
@FlywayTest(locationsForMigrate = "db/migration/h2")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    FlywayTestExecutionListener.class })
public class DAOTest {

	@Autowired
	private ConfigDAO configDAO;
	
	@Autowired
	private SnapshotDAO snapshotDAO;
	
	
	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testConfigNoParentFound() {
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName")
				.readonly(true)
				.tags("tags")
				.build();
		
		Config config = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv))
				.description("description")
				.system("system")
				.build();
		
		Node parentNode = new Node();
		parentNode.setId(1);
		config.setParent(parentNode);
		
		configDAO.createNewConfiguration(config);
	}
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolder() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Node node = new Node();
		node.setNodeType(NodeType.FOLDER);
		node.setName("Folder1");
		node.setParent(parentNode);
		
		Node newNode = configDAO.createNewFolder(node);
		
		assertNotNull(newNode);
	}
	
	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderWithDuplicateName() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		
		Node node = new Node();
		node.setNodeType(NodeType.FOLDER);
		node.setName("Folder1");
		node.setParent(parentNode);
		
		// Create a new folder
		configDAO.createNewFolder(node);
		
		// Try to create a new folder with the same name in the same parent directory
		configDAO.createNewFolder(node);
	
	}
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderNoDuplicateName() {
		
		Node parentNode = new Node();
		parentNode.setId(0);	
		
		Node node = new Node();
		node.setNodeType(NodeType.FOLDER);
		node.setName("Folder1");
		node.setParent(parentNode);
		
		Node node2 = new Node();
		node2.setNodeType(NodeType.FOLDER);
		node2.setName("Folder2");
		node2.setParent(parentNode);
		
		// Create a new folder
		configDAO.createNewFolder(node);
		
		// Try to create a new folder with the same name in the same parent directory
		configDAO.createNewFolder(node2);
	
	}
	

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewConfig() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Config config = Config.builder()
				.active(true)
				.description("description")
				.system("system")
				.build();
		
		config.setParent(parentNode);
		config.setName("My config");
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName")
				.tags("tags")
				.build();
		
		config.setConfigPvList(Arrays.asList(configPv));
	
		Config newConfig = configDAO.createNewConfiguration(config);	
		
		assertFalse(newConfig.getConfigPvList().isEmpty());
		
		int configPvId = newConfig.getConfigPvList().get(0).getId();
		
		config = Config.builder()
				.active(true)
				.description("description")
				.system("system")
				.build();
		
		config.setParent(parentNode);
		config.setName("My config 2");
		config.setConfigPvList(Arrays.asList(configPv));
		
		newConfig = configDAO.createNewConfiguration(config);
		
		// Verify that a new ConfigPv has NOT been created
		
		assertEquals(configPvId, newConfig.getConfigPvList().get(0).getId());
	}
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewConfigNoConfigPvs() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Config config = Config.builder()
				.active(true)
				.description("description")
				.system("system")
				.build();
		
		config.setParent(parentNode);
		config.setName("My config");
	
		Config newConfig = configDAO.createNewConfiguration(config);	
		
		assertTrue(newConfig.getConfigPvList().isEmpty());
	}
	
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetNonExitingConfig() {
		assertNull(configDAO.getConfig(-1));
	}
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetNode() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Node node = new Node();
		node.setNodeType(NodeType.FOLDER);
		node.setName("Folder1");
		node.setParent(parentNode);
		
		Node newNode = configDAO.createNewFolder(node);
		
		Node nodeFromDB = configDAO.getNode(newNode.getId());
		
		assertEquals(newNode, nodeFromDB);
	}
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetNodeAsConfig() {
		
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Config config = Config.builder()
				.active(true)
				.name("My config 3")
				.parent(parentNode)
				.description("description")
				.system("system")
				.build();
	
		Config newConfig = configDAO.createNewConfiguration(config);	
		
		Config configFromDB = (Config)configDAO.getNode(newConfig.getId());
		
		assertEquals(newConfig, configFromDB);
	}
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testSaveSnapshot() {
		Node parentNode = new Node();
		parentNode.setId(0);
		
		Config config = Config.builder()
				.active(true)
				.name("My config 3")
				.parent(parentNode)
				.description("description")
				.system("system")
				.configPvList(Arrays.asList(ConfigPv.builder().pvName("whatever").build()))
				.build();
		
		Config savedConfig = 
				configDAO.createNewConfiguration(config);		
		
		SnapshotPv<Integer> snapshotPv = SnapshotPv.<Integer>builder()
				.dtype(1)
				.fetchStatus(true)
				.severity(2)
				.status(3)
				.time(1000L)
				.timens(20000)
				.value(10)
				.configPv(ConfigPv.builder().pvName("whatever").id(savedConfig.getConfigPvList().get(0).getId()).build())
				.build();
		
		Snapshot snapshot = Snapshot.builder()
				.approve(true)
				.configId(config.getId())
				.snapshotPvList(Arrays.asList(snapshotPv))
				.build();
		
		Snapshot newSnapshot = configDAO.savePreliminarySnapshot(snapshot);
		
		assertEquals(10, newSnapshot.getSnapshotPvList().get(0).getValue());
		
		Snapshot fullSnapshot = snapshotDAO.getSnapshot(newSnapshot.getId());
		
		assertNull(fullSnapshot);
		
		List<Snapshot> snapshots = snapshotDAO.getSnapshots(config.getId());
		
		assertTrue(snapshots.isEmpty());
		
		snapshotDAO.commitSnapshot(newSnapshot.getId(), "user", "comment");
		
		fullSnapshot = snapshotDAO.getSnapshot(newSnapshot.getId());
		
		assertEquals(1, fullSnapshot.getSnapshotPvList().size());
		
		snapshots = snapshotDAO.getSnapshots(config.getId());
		
		assertEquals(1, snapshots.size());
		
		snapshotDAO.deleteSnapshot(newSnapshot.getId());
		
		snapshots = snapshotDAO.getSnapshots(config.getId());
		
		assertTrue(snapshots.isEmpty());
		
		newSnapshot = configDAO.savePreliminarySnapshot(snapshot);
		
		snapshotDAO.commitSnapshot(newSnapshot.getId(), "user", "comment");
	}
	

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testGetSnapshotsNoSnapshots() {
		
		List<Snapshot> snapshots = snapshotDAO.getSnapshots(-1);
		
		assertTrue(snapshots.isEmpty());
	}
	
	
	
	
	
	
}
