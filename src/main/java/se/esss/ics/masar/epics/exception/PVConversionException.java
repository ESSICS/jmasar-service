package se.esss.ics.masar.epics.exception;

public class PVConversionException extends RuntimeException {
	
	private static final long serialVersionUID = -5717295423339192670L;
	
	public PVConversionException(String message) {
		super(message);
	}
}
