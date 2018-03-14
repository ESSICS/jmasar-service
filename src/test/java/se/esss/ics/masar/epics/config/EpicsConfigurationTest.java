package se.esss.ics.masar.epics.config;

import org.epics.pvaClient.PvaClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { EpicsConfiguration.class })})
public class EpicsConfigurationTest {
	
	@Autowired
	private PvaClient pvaClient;
	
	@Test
	public void testPvaClient() {
		assertNotNull(pvaClient);
	}
}
