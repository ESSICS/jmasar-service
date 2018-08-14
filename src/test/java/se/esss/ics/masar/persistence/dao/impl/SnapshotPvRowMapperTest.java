package se.esss.ics.masar.persistence.dao.impl;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.esss.ics.masar.persistence.dao.impl.SnapshotPvRowMapper;

import static org.junit.Assert.*;

public class SnapshotPvRowMapperTest {
	
	private SnapshotPvRowMapper mapper = new SnapshotPvRowMapper(new ObjectMapper());

	@Test
	public void testGetTypedValueNull() {
		
		assertNull(mapper.getTypedValue(null, null));
	}
	
	@Test
	public void testGetTypedValueInvalidClassName() {
		assertNull(mapper.getTypedValue("1", "invalid.class.name"));
	}
	
	@Test
	public void testGetTypedValue() {
		
		Object object = mapper.getTypedValue("2.0", "java.lang.Double");
		assertTrue(object instanceof Double);
	}
}
