package com.rubicon.waterorder.exception;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ExceptionDetails {

	private String message;
	private LocalDateTime localDateTime;

	public ExceptionDetails(String message, LocalDateTime localDateTime) {
		super();
		this.message = message;
		this.localDateTime = localDateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

}
