package se.esss.ics.masar.epics.exception;

public class PVReadException extends Exception {
	
	public static final long serialVersionUID = 1;
	
	public PVReadException(String message) {
		super(message);
	}
}
