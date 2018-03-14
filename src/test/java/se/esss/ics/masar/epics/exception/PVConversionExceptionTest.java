package se.esss.ics.masar.epics.exception;

import org.junit.Test;
import static org.junit.Assert.*;

import se.esss.ics.masar.epics.exception.PVConversionException;

public class PVConversionExceptionTest {
	
	@Test
	public void test() {
		PVConversionException exception = new PVConversionException("message");
		
		assertEquals("message", exception.getMessage());
	}
}
