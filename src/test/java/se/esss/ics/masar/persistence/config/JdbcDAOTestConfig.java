package se.esss.ics.masar.persistence.config;


import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.persistence.dao.impl.ConfigJdbcDAO;
import se.esss.ics.masar.persistence.dao.impl.SnapshotJdbcDAO;

@Configuration
public class JdbcDAOTestConfig {


	@Bean
	public ConfigDAO configDAO() {

		return new ConfigJdbcDAO();
	}
	
	@Bean
	public SnapshotDAO snapshotDAO() {
		return new SnapshotJdbcDAO();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return Mockito.mock(JdbcTemplate.class);
	}
	
	@Bean
	public SimpleJdbcInsert configurationEntryRelationInsert() {
		return Mockito.mock(SimpleJdbcInsert.class);
	}
	
	@Bean
	public SimpleJdbcInsert configurationInsert() {
		return Mockito.mock(SimpleJdbcInsert.class);
	}

	@Bean
	public SimpleJdbcInsert configurationEntryInsert() {
		return Mockito.mock(SimpleJdbcInsert.class);
	}
	
	@Bean
	public SimpleJdbcInsert snapshotPvInsert() {
		return Mockito.mock(SimpleJdbcInsert.class);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
