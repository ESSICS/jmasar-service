package se.esss.ics.masar.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Config extends Node{

	private boolean active = true;
	private String description;
	private String system;
	
	@Builder
	public Config(int id, 
			String name, 
			Date created, 
			Date lastModified, 
			Node parent,
			List<Node> childNodes,
			boolean active,
			String description,
			String system,
			List<ConfigPv> configPvList){
		super(childNodes, parent, id, name ,created, lastModified, NodeType.CONFIGURATION);
		
		this.active = active;
		this.description = description;
		this.system = system;
		this.configPvList = configPvList;
	}

	@NotNull
	private List<ConfigPv> configPvList;
	
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Config) {
			Config config = (Config)other;
			return getId() == config.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return getId();
	}
}
