package se.esss.ics.masar.model.exception;

public class ConfigNotFoundException extends RuntimeException {

	public static final long serialVersionUID = 1;
	
	public ConfigNotFoundException(String message) {
		super(message);
	}
}
