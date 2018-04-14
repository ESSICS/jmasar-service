package se.esss.ics.masar.services.exception;

public class SnapshotNotFoundException extends RuntimeException {

	public static final long serialVersionUID = 1;
	
	public SnapshotNotFoundException(String message) {
		super(message);
	}
}
