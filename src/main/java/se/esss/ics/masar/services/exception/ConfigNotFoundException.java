package se.esss.ics.masar.services.exception;

public class ConfigNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5778552461735183293L;
	
	public ConfigNotFoundException(String message) {
		super(message);
	}
}
