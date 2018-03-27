package se.esss.ics.masar.model.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.Assert.*;

public class ConfigTest {

	@Test
	public void testConfig() {
		Date now = new Date();
		Config config = Config.builder()
				.description("description")
				.system("system")
				.build();
		
		assertTrue(config.isActive());
		assertEquals("description", config.getDescription());
		assertEquals(now, config.getCreated());
		assertEquals(1, config.getId());
		assertEquals("system", config.getSystem());
		assertNull(config.getConfigPvList());
		
		config = Config.builder()
				.active(false)
				.description("description")
				.build();
		
		assertFalse(config.isActive());
		assertEquals("description", config.getDescription());
		assertEquals(now, config.getCreated());
		assertEquals(1, config.getId());
		assertNull(config.getSystem());
		assertNull(config.getConfigPvList());
	}
	
	@Test
	public void testConfigPv() {
		
		ConfigPv configPv = ConfigPv.builder()
				.groupname("groupname")
				.id(7)
				.pvName("pvName")
				.tags("tags")
				.build();
		
		assertFalse(configPv.isReadonly());
		assertEquals(7, configPv.getId());
		assertEquals("pvName", configPv.getPvName());
		assertEquals("tags", configPv.getTags());
		
		
		Date now = new Date();
		Config config = Config.builder()
				.description("description")
				.system("system")
				.configPvList(Arrays.asList(configPv))
				.build();
		
		assertEquals(1, config.getConfigPvList().size());
	}
}
