package se.esss.ics.masar.model.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.node.NodeData;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config extends NodeData{

	@Builder.Default
	private boolean active = true;
	private String description;
	private String system;
	
	private List<ConfigPv> configPvList;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public List<ConfigPv> getConfigPvList() {
		return configPvList;
	}

	public void setConfigPvList(List<ConfigPv> configPvList) {
		this.configPvList = configPvList;
	}
	
	
	
}
