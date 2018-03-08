package se.esss.ics.masar.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.esss.ics.masar.services.IServices;
import se.esss.ics.masar.services.impl.Services;

@Configuration
public class WebConfiguration {

	@Bean
	public IServices services() {
		return new Services();
	}
}
