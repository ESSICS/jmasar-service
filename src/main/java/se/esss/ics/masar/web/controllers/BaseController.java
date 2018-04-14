package se.esss.ics.masar.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import se.esss.ics.masar.services.exception.ConfigNotFoundException;
import se.esss.ics.masar.services.exception.SnapshotNotFoundException;

@RestController
public abstract class BaseController {

	@ExceptionHandler(ConfigNotFoundException.class)
	public ResponseEntity<String> handleConfigNotFoundException(HttpServletRequest req,
			ConfigNotFoundException exception) {

		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SnapshotNotFoundException.class)
	public ResponseEntity<String> handleSnapshotNotFoundException(HttpServletRequest req,
			SnapshotNotFoundException exception) {

		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(HttpServletRequest req,
			IllegalArgumentException exception) {

		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
