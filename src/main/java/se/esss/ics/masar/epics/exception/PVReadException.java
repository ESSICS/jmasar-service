package se.esss.ics.masar.epics.exception;

public class PVReadException extends Exception {
	
	private static final long serialVersionUID = 6141732470811839272L;	
	
	public PVReadException(String message) {
		super(message);
	}
}
