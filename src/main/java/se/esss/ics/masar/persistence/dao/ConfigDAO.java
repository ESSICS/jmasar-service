package se.esss.ics.masar.persistence.dao;

import java.util.List;

import se.esss.ics.masar.model.config.Config;
import se.esss.ics.masar.model.config.ConfigPv;


public interface ConfigDAO {
	
	public int saveConfig(Config config);
	
	public List<Config> getConfigs();
	
	public Config getConfig(int configId);
	
	public List<ConfigPv> getConfigPvs(int configId);
		
}
