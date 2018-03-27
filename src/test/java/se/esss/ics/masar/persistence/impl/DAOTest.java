package se.esss.ics.masar.persistence.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
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

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.node.Node;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;
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
	
	
	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testConfig() {
		
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
//		
//		int configId = configDAO.createNewConfiguration(config);
//		
//		assertTrue(configId > 0);
//		
//		config = configDAO.getConfig(configId);
//		
//		assertEquals(1, config.getConfigPvList().size());
		
//		Config config2 = Config.builder()
//				.active(true)
//				.configPvList(Arrays.asList(configPv))
//				.description("description")
//				.system("system")
//				.build();
//		
//		int configId2 = configDAO.createNewConfiguration(config2);
//		
//		assertTrue(configId != configId2);
	}
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetConfigs() {
		List<Config> configs = configDAO.getConfigs();
		assertEquals(2, configs.size());
	}
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testGetNonExitingConfig() {
		assertNull(configDAO.getConfig(-1));
	}
	
	@Test
	@FlywayTest(invokeCleanDB = false)
	public void testSnapshot() {
		
		assertTrue(snapshotDAO.getSnapshots(-1).isEmpty());
		
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
//		
//		int configId = configDAO.createNewConfiguration(config);
//		
//		SnapshotPv snapshotPv = SnapshotPv.builder()
//				.dtype(1)
//				.fetchStatus(true)
//				.severity(0)
//				.status(1)
//				.time(1000L)
//				.timens(777)
//				.value(new Double(7.7))
//				.build();
//		
//		Snapshot snapshot = new Snapshot();
//		snapshot.setApprove(true);
//		snapshot.setComment("comment");
//		snapshot.setCreated(new Date());
//		snapshot.setSnapshotPvList(Arrays.asList(snapshotPv));
//				
//		
//		Node node = configDAO.savePreliminarySnapshot(snapshot);
//		
//		assertNotNull(node);
		
//		Snapshot snapshot2 = snapshotDAO.getSnapshot(snapshotId);
//		
//		assertNull(snapshot2);
//		
//		snapshotDAO.commitSnapshot(snapshotId, "Mr X", "comment");
//		
//		snapshot2 = snapshotDAO.getSnapshot(snapshotId);
//		
//		assertNotNull(snapshot2);
//		assertEquals("Mr X", snapshot2.getUserName());
//		assertEquals("comment", snapshot2.getComment());
//		
//		assertEquals(1, snapshot2.getSnapshotPvList().size());
//		
//		assertEquals(1, snapshotDAO.getSnapshots(configId).size());
//		
//		snapshotDAO.deleteSnapshot(snapshotId);
//		
//		assertNull(snapshotDAO.getSnapshot(snapshotId));
	}
	
	
	
	
}
