package se.esss.ics.masar.model.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPv {
	
	private int id;
	private String pvName;
	
	@Builder.Default
	private boolean readonly = false;
	private String tags;
	private String groupname;
	
}
