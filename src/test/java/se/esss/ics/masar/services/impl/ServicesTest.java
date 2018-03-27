package se.esss.ics.masar.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.exception.ConfigNotFoundException;
import se.esss.ics.masar.model.node.Node;
import se.esss.ics.masar.model.snapshot.Snapshot;
import se.esss.ics.masar.model.snapshot.SnapshotPv;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.config.ServicesTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { ServicesTestConfig.class}) })
public class ServicesTest {
	
	@Autowired
	private IServices services;
	
	@Autowired
	private ConfigDAO configDAO;
	
	@Autowired
	private SnapshotDAO snapshotDAO;
	
	private Config configFromClient;
	
	private Config config1;
	
	private Snapshot snapshot1;
	
	@Before
	public void setUp() {
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.pvName("pvName")
				.readonly(true)
				.tags("tags")
				.build();
		
		configFromClient = Config.builder()
				.active(true)
				.configPvList(Arrays.asList(configPv))
				.description("description")

				.system("system")
				.build();
		
		config1 = Config.builder()
				.active(true)
	
				.configPvList(Arrays.asList(configPv))
	
				.description("description")
			
				.system("system")
				.build();
		
		//when(configDAO.createNewConfiguration(configFromClient)).thenReturn(1);
		when(configDAO.getConfig(1)).thenReturn(config1);
		when(configDAO.getConfig(2)).thenReturn(null);
		
		when(configDAO.getConfigs()).thenReturn(Arrays.asList(config1));
		
		SnapshotPv snapshotPv = SnapshotPv.builder()
				.dtype(1)
				.fetchStatus(true)
				.severity(0)
				.status(1)
				.time(1000L)
				.timens(777)
				.value(new Double(7.7))
				.build();
		
		 
	}
	
	
	@Test
	public void testSaveNewSnapshot() {
		
		Node config = services.createNewConfiguration(configFromClient);
		
		assertEquals(1, config.getId());
		assertNotNull(config.getCreated());
	}
	
	@Test
	public void testGetConfigs() {
		
		List<Config> configs = services.getConfigs();
		
		assertEquals(1, configs.size());

	}
	
	@Test
	public void testGetConfigNotNull() {
		
		Config config = services.getConfig(1);
		assertEquals(1, config.getId());
	}
	
	@Test(expected = ConfigNotFoundException.class)
	public void testGetConfigNull() {
		
		services.getConfig(-1);
	
	}
	
	@Test
	public void testTakeSnapshot() {
		
		services.takeSnapshot(1);
	}
}
