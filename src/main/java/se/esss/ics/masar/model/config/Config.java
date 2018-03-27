package se.esss.ics.masar.model.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.esss.ics.masar.model.NodeType;
import se.esss.ics.masar.model.node.Node;

@Builder
@AllArgsConstructor
@Data
public class Config extends Node{

	@Builder.Default
	private boolean active = true;
	private String description;
	private String system;
	private List<ConfigPv> configPvList;
	
	public Config() {
		super.setNodeType(NodeType.CONFIGURATION);
	}
}
