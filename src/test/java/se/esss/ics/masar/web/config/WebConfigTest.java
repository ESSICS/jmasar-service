package se.esss.ics.masar.web.config;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;

import se.esss.ics.masar.epics.config.EpicsConfiguration;
import se.esss.ics.masar.persistence.config.PersistenceConfiguration;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.config.ServicesConfiguration;

@RunWith(SpringRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = {
		EpicsConfiguration.class,
		PersistenceConfiguration.class, 
		ServicesConfiguration.class, 
		WebConfiguration.class})})
public class WebConfigTest {
	
	@Autowired
	private IServices services;
	
	@BeforeClass
	public static void init() {
		System.setProperty("dbengine", "h2");
	}

	@Test
	public void testWbeConfig() {
		Assert.assertNotNull(services);
	}
}
