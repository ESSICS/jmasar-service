package se.esss.ics.masar.model;

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
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof ConfigPv) {
			ConfigPv configPv = (ConfigPv)other;
			return id == configPv.getId();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
