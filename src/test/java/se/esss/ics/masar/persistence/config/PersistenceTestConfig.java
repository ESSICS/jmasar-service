package se.esss.ics.masar.persistence.config;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.persistence.dao.impl.ConfigJdbcDAO;
import se.esss.ics.masar.persistence.dao.impl.SnapshotJdbcDAO;

@Configuration
public class PersistenceTestConfig {


	@Bean
	public ConfigDAO configDAO() {

		return new ConfigJdbcDAO();
	}
	
	@Bean
	public SnapshotDAO snapshotDAO() {
		return new SnapshotJdbcDAO();
	}

	
	@Bean("flyway")
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		
		flyway.setDataSource("jdbc:h2:nio:./db/h2.db", "", "");
		flyway.setLocations("db/migration/h2");
		flyway.setIgnoreFailedFutureMigration(false);
		flyway.setIgnoreFailedFutureMigration(true);
		flyway.setValidateOnMigrate(true);
		flyway.setOutOfOrder(false);
		
		return flyway;
	}
}
