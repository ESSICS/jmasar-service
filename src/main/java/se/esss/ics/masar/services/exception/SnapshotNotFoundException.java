package se.esss.ics.masar.services.exception;

public class SnapshotNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -980968730210448760L;
	
	public SnapshotNotFoundException(String message) {
		super(message);
	}
}
