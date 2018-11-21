package se.esss.ics.masar.epics.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PVReadExceptionTest {
	
	@Test
	public void test() {
		PVReadException exception = new PVReadException("message");
		
		assertEquals("message", exception.getMessage());
	}
}
