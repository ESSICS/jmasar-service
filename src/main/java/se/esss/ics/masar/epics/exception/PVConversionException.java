package se.esss.ics.masar.epics.exception;

public class PVConversionException extends RuntimeException {
	
	public static final long serialVersionUID = 1;
	
	public PVConversionException(String message) {
		super(message);
	}
}
