package se.esss.ics.masar.persistence.impl;

import static org.assertj.core.api.Assertions.fail;
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
import se.esss.ics.masar.model.Folder;
import se.esss.ics.masar.model.Node;
import se.esss.ics.masar.model.Snapshot;
import se.esss.ics.masar.model.SnapshotPv;
import se.esss.ics.masar.persistence.config.PersistenceConfiguration;
import se.esss.ics.masar.persistence.config.PersistenceTestConfig;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableConfigurationProperties
@ContextHierarchy({ @ContextConfiguration(classes = { PersistenceConfiguration.class, PersistenceTestConfig.class }) })
@TestPropertySource(properties = { "dbengine = h2" })
@FlywayTest(locationsForMigrate = "db/migration/h2")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class })
public class DAOTest {

	@Autowired
	private ConfigDAO configDAO;

	@Autowired
	private SnapshotDAO snapshotDAO;

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testCreateConfigNoParentFound() {

		ConfigPv configPv = ConfigPv.builder().groupname("groupname").pvName("pvName").readonly(true).tags("tags")
				.build();

		Config config = Config.builder().active(true).configPvList(Arrays.asList(configPv)).description("description")
				.system("system").build();

		Node parentNode = new Node();
		parentNode.setId(1);
		config.setParent(parentNode);

		configDAO.createConfiguration(config);
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolder() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").id(11).parent(Folder.builder().id(0).build())
				.build();

		Node newNode = configDAO.createFolder(folderFromClient);

		assertNotNull(newNode);
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderWrongParentId() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").id(11).parent(Folder.builder().id(-1).build())
				.build();

		configDAO.createFolder(folderFromClient);

	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderNoParent() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").id(11).build();

		configDAO.createFolder(folderFromClient);

	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderWithDuplicateName() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").id(11).parent(Folder.builder().id(0).build())
				.build();

		// Create a new folder
		configDAO.createFolder(folderFromClient);

		// Try to create a new folder with the same name in the same parent directory
		configDAO.createFolder(folderFromClient);

	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewFolderNoDuplicateName() {

		Node parentNode = new Node();
		parentNode.setId(0);

		Folder folder1 = Folder.builder().name("Folder 1").parent(Folder.builder().id(0).build()).build();

		Folder folder2 = Folder.builder().name("Folder 2").parent(Folder.builder().id(0).build()).build();

		// Create a new folder
		configDAO.createFolder(folder1);

		// Try to create a new folder with the same name in the same parent directory
		configDAO.createFolder(folder2);

	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewConfig() {

		Node parentNode = new Node();
		parentNode.setId(0);
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName")
				.tags("tags")
				.build();

		Config config = Config.builder()
				.active(true)
				.description("description")
				.system("system")
				.parent(parentNode)
				.name("My config")
				.configPvList(Arrays.asList(configPv))
				.build();

		Config newConfig = configDAO.createConfiguration(config);

		assertFalse(newConfig.getConfigPvList().isEmpty());

		int configPvId = newConfig.getConfigPvList().get(0).getId();

		config = Config.builder()
				.active(true)
				.description("description")
				.system("system")
				.parent(parentNode)
				.name("My config 2")
				.configPvList(Arrays.asList(configPv))
				.build();

		newConfig = configDAO.createConfiguration(config);

		// Verify that a new ConfigPv has NOT been created

		assertEquals(configPvId, newConfig.getConfigPvList().get(0).getId());
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testNewConfigNoConfigPvs() {

		Node node = new Node();
		node.setId(0);

		Config config = Config.builder().active(true).description("description").system("system").build();

		config.setParent(node);
		config.setName("My config");

		Config newConfig = configDAO.createConfiguration(config);

		assertTrue(newConfig.getConfigPvList().isEmpty());
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testDeleteConfiguration() {

		Node parentNode = new Node();
		parentNode.setId(0);

		Config config = Config.builder().active(true).description("description").system("system").build();

		config.setParent(parentNode);
		config.setName("My config");

		config = configDAO.createConfiguration(config);

		configDAO.deleteConfiguration(config.getId());

		try {
			configDAO.getConfiguration(config.getId());
			fail("IllegalArgumentException expected here");
		} catch (IllegalArgumentException e) {
			// Expected = OK
		}
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testDeleteConfigurationAndPvs() {

		Node parentNode = new Node();
		parentNode.setId(0);

		ConfigPv configPv = ConfigPv.builder().groupname("group").readonly(true).tags("tags").pvName("pvName").build();

		Config config = Config.builder().active(true).description("description").system("system")
				.configPvList(Arrays.asList(configPv)).build();

		config.setParent(parentNode);
		config.setName("My config");

		config = configDAO.createConfiguration(config);

		configDAO.deleteConfiguration(config.getId());

		try {
			configDAO.getConfiguration(config.getId());
			fail("IllegalArgumentException expected here");
		} catch (IllegalArgumentException e) {
			// Expected = OK
		}

	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testDeleteNonExistingFolder() {

		configDAO.deleteFolder(-1);
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testDeleteFolder() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(0).build()).build();

		Folder folder1 = configDAO.createFolder(folderFromClient);

		folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(folder1.getId()).build())
				.build();

		Folder folder2 = configDAO.createFolder(folderFromClient);

		Config config = configDAO
				.createConfiguration(Config.builder().name("Config").description("Desc").parent(folder2).build());

		configDAO.deleteFolder(folder1.getId());

		try {
			configDAO.getFolder(folder1.getId());
			fail("IllegalArgumentException expected here.");
		} catch (IllegalArgumentException e) {
			// Expected = OK
		}

		try {
			configDAO.getFolder(folder2.getId());
			fail("IllegalArgumentException expected here.");
		} catch (IllegalArgumentException e) {
			// Expected = OK
		}

		try {
			configDAO.getConfiguration(config.getId());
			fail("IllegalArgumentException expected here.");
		} catch (IllegalArgumentException e) {
			// Expected = OK
		}
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testDeleteConfigurationLeaveReferencedPVs() {

		Node parentNode = new Node();
		parentNode.setId(0);

		ConfigPv configPv1 = ConfigPv.builder().groupname("group").readonly(true).tags("tags").pvName("pvName").build();

		ConfigPv configPv2 = ConfigPv.builder().groupname("group").readonly(true).tags("tags").pvName("pvName2")
				.build();

		Config config1 = Config.builder().active(true).description("description").system("system")
				.configPvList(Arrays.asList(configPv1, configPv2)).build();

		config1.setParent(parentNode);
		config1.setName("My config");

		config1 = configDAO.createConfiguration(config1);

		Config config2 = Config.builder().active(true).description("description").system("system")
				.configPvList(Arrays.asList(configPv2)).build();

		config2.setParent(parentNode);
		config2.setName("My config 2");

		config2 = configDAO.createConfiguration(config2);

		configDAO.deleteConfiguration(config1.getId());

		Config config = configDAO.getConfiguration(config2.getId());

		assertEquals(1, config.getConfigPvList().size());

	}

	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetNodeAsConfig() {

		Node parentNode = new Node();
		parentNode.setId(0);

		Config config = Config.builder().active(true).name("My config 3").parent(parentNode).description("description")
				.system("system").build();

		Config newConfig = configDAO.createConfiguration(config);

		Config configFromDB = configDAO.getConfiguration(newConfig.getId());

		assertEquals(newConfig, configFromDB);
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = false)
	public void testGetNonExistingConfig() {
		configDAO.getConfiguration(-1);
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testSaveSnapshot() {
		Node parentNode = new Node();
		parentNode.setId(0);

		Config config = Config.builder().active(true).name("My config 3").parent(parentNode).description("description")
				.system("system").configPvList(Arrays.asList(ConfigPv.builder().pvName("whatever").build())).build();

		Config savedConfig = configDAO.createConfiguration(config);

		SnapshotPv<Integer> snapshotPv = SnapshotPv.<Integer>builder().dtype(1).fetchStatus(true).severity(2).status(3)
				.time(1000L).timens(20000).value(10)
				.configPv(
						ConfigPv.builder().pvName("whatever").id(savedConfig.getConfigPvList().get(0).getId()).build())
				.build();

		Snapshot snapshot = Snapshot.builder().approve(true).configId(config.getId())
				.snapshotPvList(Arrays.asList(snapshotPv)).build();

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

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testGetNonexistingFolder() {

		configDAO.getFolder(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testMoveNodeIllegalSource() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(0).build()).build();

		Folder folder1 = configDAO.createFolder(folderFromClient);

		configDAO.moveNode(-1, folder1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testMoveNodeIllegalTarget() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(0).build()).build();

		Folder folder1 = configDAO.createFolder(folderFromClient);

		configDAO.moveNode(folder1.getId(), -1);
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testMoveNodeNameClash1() {

		Folder folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(0).build()).build();

		Folder folder1 = configDAO.createFolder(folderFromClient);

		folderFromClient = Folder.builder().name("SomeFolder").parent(Folder.builder().id(folder1.getId()).build())
				.build();

		Folder folder2 = configDAO.createFolder(folderFromClient);

		configDAO.createConfiguration(Config.builder().name("Config").description("Desc").parent(folder2).build());

		configDAO.moveNode(folder2.getId(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	@FlywayTest(invokeCleanDB = true)
	public void testMoveNodeNameClash2() {

		Folder root = Folder.builder().id(0).build();

		configDAO.createConfiguration(Config.builder().name("Config").description("Desc").parent(root).build());

		Folder folder1 = Folder.builder().name("SomeFolder").parent(root).build();

		Config config2 = configDAO
				.createConfiguration(Config.builder().name("Config").description("Desc").parent(folder1).build());

		configDAO.moveNode(config2.getId(), 0);

	}
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testMoveNode() {
		
		Folder root = Folder.builder().id(0).build();

		Folder folder1 = 
				Folder.builder().name("SomeFolder").parent(root).build();

		folder1 = configDAO.createFolder(folder1);

		Folder folder2 = 
				Folder.builder().name("SomeFolder2").parent(folder1).build();

		folder2 = configDAO.createFolder(folder2);
		
		Config config = 
				configDAO.createConfiguration(Config.builder().name("Config").description("Desc").parent(folder2).build());

		configDAO.moveNode(folder2.getId(), 0);
	}

}
