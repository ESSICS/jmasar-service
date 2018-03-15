package se.esss.ics.masar.web.config;

import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import se.esss.ics.masar.services.IServices;

@SpringBootConfiguration
@ComponentScan(basePackages = "se.esss.ics.masar.web.controllers")
public class ControllersTestConfig {

	@Bean
	public IServices services() {
		return Mockito.mock(IServices.class);
	}
}
