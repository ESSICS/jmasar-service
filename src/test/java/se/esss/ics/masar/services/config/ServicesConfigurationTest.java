package se.esss.ics.masar.services.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.config.EpicsConfiguration;
import se.esss.ics.masar.persistence.config.PersistenceConfiguration;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { ServicesConfiguration.class, PersistenceConfiguration.class, EpicsConfiguration.class}) })
@TestPropertySource(properties = {"dbengine = h2"})
public class ServicesConfigurationTest {
	
	@Autowired
	private IEpicsService epicsService;
	
	@Autowired
	private ConfigDAO configDAO;
	
	@Autowired
	private SnapshotDAO snapshotDAO;
	
	@Test
	public void testConfig() {
		assertNotNull(epicsService);
		assertNotNull(configDAO);
		assertNotNull(snapshotDAO);
	}
}
