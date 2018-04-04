package se.esss.ics.masar.epics.config;

import org.epics.pvaClient.PvaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EpicsConfiguration {
	
	@Bean
	public PvaClient pvaClient() {
		return PvaClient.get("pva ca");
	}
}
