package com.HotelBooking.exception;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BookingNotFoundException.class)
	protected ResponseEntity<ErrorMessage> handleBookingNotFound(BookingNotFoundException ex, WebRequest request){
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}
	
			
}
