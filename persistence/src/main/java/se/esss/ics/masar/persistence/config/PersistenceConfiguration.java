package se.esss.ics.masar.persistence.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/${dbengine}.properties")
public class PersistenceConfiguration {

	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public HikariDataSource dataSource() {
	    return (HikariDataSource) DataSourceBuilder.create()
	            .type(HikariDataSource.class).build();
	}
	
	@Bean
	public SimpleJdbcInsert configurationInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("config").usingGeneratedKeyColumns("id");
	}
	
	@Bean
	public SimpleJdbcInsert configurationEntryInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("config_pv").usingGeneratedKeyColumns("id");
	}
	
	@Bean
	public SimpleJdbcInsert configurationEntryRelationInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("config_pv_relation");
	}
	
	@Bean
	public SimpleJdbcInsert snapshotInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("snapshot").usingGeneratedKeyColumns("id");
	}
	
	@Bean
	public SimpleJdbcInsert snapshotPvInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("snapshot_pv").usingGeneratedKeyColumns("id");
	}
	
	@Bean
	public SimpleJdbcInsert userNameInsert() {
		DataSource dataSource = dataSource();
		
		return new SimpleJdbcInsert(dataSource).withTableName("userName").usingGeneratedKeyColumns("id");
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		DataSource dataSource = dataSource();
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
}