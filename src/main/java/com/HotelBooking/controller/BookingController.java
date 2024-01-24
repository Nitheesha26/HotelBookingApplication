package com.HotelBooking.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HotelBooking.exception.BookingNotFoundException;
import com.HotelBooking.model.Booking;
import com.HotelBooking.service.BookingService;

/**
 * Controller class for handling httpMethods(GET,PUT,POST,DELETE) hotel booking system.
 */

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@Autowired
    private BookingService bookingService;
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<String> handleBookingNotFound(BookingNotFoundException ex){
		logger.error("Booking not found: {}",ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	// Endpoint to create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
    	try {
    		Booking createdBooking = bookingService.createBooking(booking);
    		logger.info("Created Booking: {}", createdBooking);
    		return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    	}catch (IllegalArgumentException e) {
			logger.error("Invalid input for creating a booking: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			logger.error("Error occurred while creating a booking", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}   
    }
	   
   // Endpoint to retrieve all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
    	try {
    		List<Booking> bookings = bookingService.getAllBookings();
    		logger.info("Retriveing all bookings",bookings );
            return ResponseEntity.status(HttpStatus.OK).body(bookings);
    	}catch (Exception e) {
    		logger.error("Error occurred while retrieving all bookings: {}", e.getMessage());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}    
    }
    
   // Endpoint to retrieve a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") String id) {
    	try {
    		Booking booking = bookingService.getBookingById(id);
            if (booking != null) {
            	logger.info("retrieving a booking by ID");
            	return ResponseEntity.status(HttpStatus.OK).body(booking);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(booking);
            }
    	}catch (Exception e) {
    		logger.error("Error occurred while retrieving booking by ID {}: {}", id, e.getMessage());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}       
    }
    
   // Endpoint to update an existing booking
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") String id, @RequestBody Booking updatedBooking) {
    	try {
    		Booking booking = bookingService.updateBooking(id, updatedBooking);
            if (booking != null) {
            	logger.info("updating a booking by ID");
                return ResponseEntity.status(HttpStatus.OK).body(booking);
            } else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(booking);
            }
    	}catch (Exception e) {
    		logger.error("Error occurred while updating booking by ID {}: {}", id, e.getMessage());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}       
    }
    
   // Endpoint to cancel a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") String id) {
        try {
            bookingService.cancelBooking(id);
            logger.info("Canceling a booking by ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found while canceling by ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while canceling booking by ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
}
