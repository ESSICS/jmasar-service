package se.esss.ics.masar.epics.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PVConversionExceptionTest {
	
	@Test
	public void test() {
		PVConversionException exception = new PVConversionException("message");
		
		assertEquals("message", exception.getMessage());
	}
}
