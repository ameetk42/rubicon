package com.rubicon.waterorder.exception;

public class ApiRequestException extends RuntimeException{

	public ApiRequestException() {
		super();
	}


	public ApiRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiRequestException(String message) {
		super(message);
	}
}
