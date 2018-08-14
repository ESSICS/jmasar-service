package se.esss.ics.masar.services.exception;

public class NodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1828621412391490962L;
	
	public NodeNotFoundException(String message) {
		super(message);
	}
}
