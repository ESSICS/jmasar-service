package se.esss.ics.masar.persistence.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(classes = { PersistenceConfiguration.class }) })
@TestPropertySource(properties = {"dbengine = h2"})
public class PersistenceConfigurationTest {
	
	@Autowired
	private HikariDataSource dataSource;
	
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
	private SimpleJdbcInsert userNameInsert;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	

	@Test
	public void test() {
		assertNotNull(dataSource);
		assertNotNull(configurationInsert);
		assertEquals("config", configurationInsert.getTableName());
		assertNotNull(configurationEntryInsert);
		assertEquals("config_pv", configurationEntryInsert.getTableName());
		assertNotNull(configurationEntryRelationInsert);
		assertEquals("config_pv_relation", configurationEntryRelationInsert.getTableName());
		assertNotNull(snapshotInsert);
		assertEquals("snapshot", snapshotInsert.getTableName());
		assertNotNull(snapshotPvInsert);
		assertEquals("snapshot_pv", snapshotPvInsert.getTableName());
		assertNotNull(userNameInsert);
		assertEquals("userName", userNameInsert.getTableName());
		assertNotNull(jdbcTemplate);
		assertNotNull(objectMapper);
	}
}
