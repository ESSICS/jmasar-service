package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;
import se.esss.ics.masar.model.node.Node;
import se.esss.ics.masar.model.node.NodeData;


public interface ConfigDAO {
	
	public Node<Void> createNewFolder(Node<Void> node);
	
	public int saveConfig(Config config);
	
	public List<Config> getConfigs();
	
	public Config getConfig(int configId);
	
	public List<ConfigPv> getConfigPvs(int configId);
	
	public <T> List<T> getChildNodes(int nodeId);
		
}
