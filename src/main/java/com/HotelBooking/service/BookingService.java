package com.HotelBooking.service;

import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.HotelBooking.exception.BookingNotFoundException;
import com.HotelBooking.model.Booking;
import com.HotelBooking.repository.BookingRepository;

import jakarta.validation.Valid;

/**
 * Service class for managing hotel booking system.
 */

@Service
@Transactional
public class BookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
	
	@Autowired
    private BookingRepository bookingRepository;
	
	//Method to create a new Booking
	public Booking createBooking(@Valid Booking booking) {
		try {
			logger.info("Creating a new Booking: {}", booking);
			//save the booking to the repository
			return bookingRepository.save(booking);
		}catch (Exception e) {
			logger.error("Error occurred while creating a booking : {}", e.getMessage());
			throw new RuntimeException("Error creating booking", e);
		}
	}

	//Method to retrieve all Bookings
	public List<Booking> getAllBookings() {
		try {
			logger.info("Retriveing all bookings");
			return bookingRepository.findAll();
		}catch (Exception e) {
			logger.error("Error occurred while retrieving all bookings : {}", e.getMessage());
			throw new RuntimeException("Error retrieving all bookings", e);
		}
	}

	// Method to retrieve a booking by ID
	/**
     * Retrieves a booking by its ID.
     *
     * @param id The ID of the booking to retrieve.
     * @return The booking if found, otherwise null.
     */
	public Booking getBookingById(String id) {
	    try {
	        logger.info("retrieving a booking by ID: {}", id);
	        Optional<Booking> optionalBooking = bookingRepository.findById(id);
	        if (optionalBooking.isPresent()) {
	            return optionalBooking.get();
	        } else {
	            throw new BookingNotFoundException("Booking not found with ID: " + id);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while retrieving a booking by ID {}: {}", id, e.getMessage());
	        throw new RuntimeException("Error retrieving booking by ID", e);
	    }
	}

	// Method to update an existing booking
    public Booking updateBooking(String id, Booking updatedBooking) {
    	try {
    		logger.info("Updating a booking with ID{}: {}", id, updatedBooking);
    		Optional<Booking> optionalBooking = bookingRepository.findById(id);
            if (optionalBooking.isPresent()) {
                Booking existingBooking = optionalBooking.get();
                //Update the existing booking with the details from updatedBooking
                existingBooking.setGuestName(updatedBooking.getGuestName());
                existingBooking.setCheckIn(updatedBooking.getCheckIn());
                existingBooking.setCheckOut(updatedBooking.getCheckOut());
                //save the updated booking to the repository
                return bookingRepository.save(existingBooking);
            } else {
            	// throw an exception if the booking with the given ID is not found
                throw new BookingNotFoundException("Booking not found with ID: "+id); 
            }
    	}catch (Exception e) {
    		logger.error("Error occurred while updating a booking with ID {}: {}", id, e.getMessage());
    		//throw a runtime exception with a generic error message
			throw new RuntimeException("Error updating booking",e);
		} 
    }

   // Method to cancel a booking
    public void cancelBooking(String id) {
        try {
            logger.info("Canceling a booking with ID: {}", id);
            Optional<Booking> optionalBooking = bookingRepository.findById(id);
            if (optionalBooking.isPresent()) {
                bookingRepository.deleteById(id);
            } else {
                throw new BookingNotFoundException("Booking not found with ID: " + id);
            }
        } catch (BookingNotFoundException e) {          
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while canceling a booking with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error canceling booking", e);
        }
    }
}
