package com.HotelBooking.exception;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
	
	private HttpStatus status;
	private String message;
	private String details;
	private Date timestamp;
	
	public ErrorMessage(HttpStatus status, String message, String details) {
		this.status = status;
		this.message = message;
		this.details = details;
		this.timestamp = new Date();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
