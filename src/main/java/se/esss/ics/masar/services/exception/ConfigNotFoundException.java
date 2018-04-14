package se.esss.ics.masar.services.exception;

public class ConfigNotFoundException extends RuntimeException {

	public static final long serialVersionUID = 1;
	
	public ConfigNotFoundException(String message) {
		super(message);
	}
}
