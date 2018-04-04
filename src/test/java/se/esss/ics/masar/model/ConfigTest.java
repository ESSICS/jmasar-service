package se.esss.ics.masar.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import se.esss.ics.masar.model.Config;
import se.esss.ics.masar.model.ConfigPv;

public class ConfigTest {

	@Test
	public void testConfig() {
		Config config = Config.builder()
				.description("description")
				.system("system")
				.configPvList(Collections.emptyList())
				.build();
		config.setId(1);
		
		assertFalse(config.isActive());
		assertEquals("description", config.getDescription());
		assertEquals("system", config.getSystem());
		assertTrue(config.getConfigPvList().isEmpty());
		
		Config config2 = Config.builder()
				.active(false)
				.description("description")
				.configPvList(Collections.emptyList())
				.build();
		config2.setId(2);
		
		assertFalse(config2.isActive());
		assertEquals("description", config2.getDescription());
		assertNull(config2.getSystem());
		assertTrue(config2.getConfigPvList().isEmpty());
		
		assertNotEquals(config, config2);
		
		assertEquals(config, config);
		
		assertNotEquals(config, "String");
		
		assertEquals(1, config.hashCode());
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
		assertEquals("groupname", configPv.getGroupname());
		
		Config config = Config.builder()
				.description("description")
				.system("system")
				.configPvList(Arrays.asList(configPv))
				.build();
		
		assertEquals(1, config.getConfigPvList().size());
	}
	
	@Test
	public void testConfigPvEquals() {
		
		ConfigPv configPv = new ConfigPv();
		configPv.setId(1);
		
		ConfigPv configPv2 = new ConfigPv();
		configPv2.setId(2);
		
		assertNotEquals(configPv, configPv2);
		
		assertEquals(configPv, configPv);
		
		assertEquals(1, configPv.hashCode());
		
		assertNotEquals(configPv, "String");
		
	}
}
