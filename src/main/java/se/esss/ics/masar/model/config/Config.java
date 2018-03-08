package se.esss.ics.masar.model.config;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Config {

	private int id;
	private String name;
	
	@Builder.Default
	private boolean active = true;
	private Date created;
	private String description;
	private String system;
	
	private List<ConfigPv> configPvList;
	
}
