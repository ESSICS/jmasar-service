package se.esss.ics.masar.services.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.esss.ics.masar.epics.IEpicsService;
import se.esss.ics.masar.persistence.dao.ConfigDAO;
import se.esss.ics.masar.persistence.dao.SnapshotDAO;
import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.impl.Services;

@Configuration
public class ServicesTestConfig {
	
	@Bean
	public IEpicsService epicsService() {
		return mock(IEpicsService.class);
	}
	
	@Bean
	public ConfigDAO configDAO() {
		return mock(ConfigDAO.class);
	}
	
	@Bean
	public SnapshotDAO snapshotDAO() {
		return mock(SnapshotDAO.class);
	}
	
	@Bean
	public IServices services() {
		return new Services();
	}
	
}
