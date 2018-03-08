package se.esss.ics.masar.model.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigPv {
	
	private int id;
	private String pvName;
	
	@Builder.Default
	private boolean readonly = false;
	private String tags;
	private String groupname;
	
}
