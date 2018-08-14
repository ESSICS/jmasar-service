package se.esss.ics.masar.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import se.esss.ics.masar.services.exception.ConfigNotFoundException;
import se.esss.ics.masar.services.exception.NodeNotFoundException;
import se.esss.ics.masar.services.exception.SnapshotNotFoundException;

@RestController
public abstract class BaseController {
	
	protected static final String JSON = "application/json;charset = UTF-8";
	
	private Logger logger = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler(ConfigNotFoundException.class)
	public ResponseEntity<String> handleConfigNotFoundException(HttpServletRequest req,
			ConfigNotFoundException exception) {
		log(exception);
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SnapshotNotFoundException.class)
	public ResponseEntity<String> handleSnapshotNotFoundException(HttpServletRequest req,
			SnapshotNotFoundException exception) {
		log(exception);
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(HttpServletRequest req,
			IllegalArgumentException exception) {
		log(exception);
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NodeNotFoundException.class)
	public ResponseEntity<String> handleNodeNotFoundException(HttpServletRequest req,
			NodeNotFoundException exception) {
		log(exception);
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	private void log(Throwable throwable) {
		logger.error("Intercepted {}", throwable.getClass().getName(), throwable);
	}
}
