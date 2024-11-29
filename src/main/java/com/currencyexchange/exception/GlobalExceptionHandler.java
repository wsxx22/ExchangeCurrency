package com.currencyexchange.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final String ERROR = "error";

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
		Map<String, String> response = new HashMap<>();
		response.put(ERROR, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<Map<String, String>> handleInsufficientFundsException(InsufficientFundsException ex) {
		Map<String, String> response = new HashMap<>();
		response.put(ERROR, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
