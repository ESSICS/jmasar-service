package se.esss.ics.masar.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.epics.impl.EpicsService;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.persistence.dao.impl.ConfigJdbcDAO;
import se.esss.ics.masar.persistence.dao.impl.SnapshotJdbcDAO;

@Configuration
public class ServicesConfiguration {

	@Bean
	public ConfigDAO configDAO() {
		return new ConfigJdbcDAO();
	}
	
	@Bean SnapshotDAO snapshotDAO() {
		return new SnapshotJdbcDAO();
	}
	
	@Bean
	public IEpicsService epicsService() {
		return new EpicsService();
	}
}
