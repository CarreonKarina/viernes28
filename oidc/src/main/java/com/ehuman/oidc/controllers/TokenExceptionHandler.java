package com.ehuman.oidc.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TokenExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandles() {
		return "error/error";
	}
	
	
	
//	@ControllerAdvice
//	public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//	    @ExceptionHandler(value  = { IllegalArgumentException.class, IllegalStateException.class })
//	    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
//	        String bodyOfResponse = "Illegal argument exception error";
//	        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
//	    }
//	  @ExceptionHandler(CarException.class)
//	  public ResponseEntity<Object> CarException(final CarException e) {
//	            String bodyOfResponse = "Car exception";
//	        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//	  }
//	}

}
