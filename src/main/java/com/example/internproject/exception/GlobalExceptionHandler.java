package com.example.internproject.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    @ResponseBody
	    public List<String> handleValidationErrors(MethodArgumentNotValidException ex) {
	        return ex.getBindingResult()
	                 .getFieldErrors()
	                 .stream()
	                 .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
	                 .collect(Collectors.toList());
	    }
	 
	// --- Spring Security: Access Denied (403) ---
	    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
	    @ResponseStatus(HttpStatus.FORBIDDEN)
	    @ResponseBody
	    public Map<String, String> handleAccessDenied(Exception ex) {
	        return Map.of("error", "Access Denied: " + ex.getMessage());
	    }

	    // --- Spring Security: Authentication failed (401) ---
	    @ExceptionHandler(AuthenticationException.class)
	    @ResponseStatus(HttpStatus.UNAUTHORIZED)
	    @ResponseBody
	    public Map<String, String> handleAuthentication(Exception ex) {
	        return Map.of("error", "Unauthorized: " + ex.getMessage());
	    }

	    // --- Handles custom ResponseStatusExceptions (like 404 Not Found) ---
	    @ExceptionHandler(ResponseStatusException.class)
	    @ResponseBody
	    public List<String> handleResponseStatusException(ResponseStatusException ex) {
	        return List.of(ex.getReason()); // wrap single reason in a list
	    }

	    // --- Handles all other exceptions ---
	    @ExceptionHandler(Exception.class)
	    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	    @ResponseBody
	    public List<String> handleGeneralException(Exception ex) {
	        // optionally log ex for debugging
	        ex.printStackTrace();
	        return List.of("An unexpected error occurred: " + ex.getMessage());
	    }
}
