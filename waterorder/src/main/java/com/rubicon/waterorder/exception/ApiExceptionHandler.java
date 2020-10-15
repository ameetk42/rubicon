package com.rubicon.waterorder.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler (ApiRequestException.class)
	public ResponseEntity<Object> handleApiException(ApiRequestException requestException) {

		ExceptionDetails exceptionDetails=new ExceptionDetails(requestException.getMessage(), LocalDateTime.now());
		
		return  new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
		
	}// handleApiException
}
