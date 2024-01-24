package com.HotelBooking.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.HotelBooking.exception.BookingNotFoundException;
import com.HotelBooking.model.Booking;
import com.HotelBooking.repository.BookingRepository;
import com.HotelBooking.service.BookingService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceTset class for Testing hotel booking system.
 */

@SpringBootTest
public class BookingServiceTest {
	
	@Mock
	private BookingRepository bookingRepository;
	
	@Autowired
	@InjectMocks
	private BookingService bookingService;
	
	@BeforeEach
	void setUp() {
	    MockitoAnnotations.openMocks(this);
	}
	
	@Test
    void testCreateBooking() {
        Booking booking = new Booking();
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
 
        Booking createdBooking = bookingService.createBooking(booking);
        assertNotNull(createdBooking);       
    }

    @Test
    void testGetAllBookings() {
        // Mock the behavior of the repository
        when(bookingRepository.findAll()).thenReturn(List.of(new Booking(), new Booking()));
        // Test the service method
        assertEquals(2, bookingService.getAllBookings().size());       
    }

    @Test
    void testGetBookingById() {
        Booking mockBooking = new Booking();
        when(bookingRepository.findById("1")).thenReturn(Optional.of(mockBooking));
            
        Booking foundBooking = bookingService.getBookingById("1");
        assertNotNull(foundBooking);
    }

    @Test
    void testUpdateBooking() {
        Booking existingBooking = new Booking();
        existingBooking.setId("1");
        when(bookingRepository.findById("1")).thenReturn(Optional.of(existingBooking));

        Booking updatedBooking = new Booking();
        updatedBooking.setId("1");
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        Booking result = bookingService.updateBooking("1", updatedBooking);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }   

    @Test
    void testCancelBooking() {
        Booking existingBooking = new Booking();
        existingBooking.setId("1");
        when(bookingRepository.findById("1")).thenReturn(Optional.of(existingBooking));

        assertDoesNotThrow(() -> bookingService.cancelBooking("1"));
        verify(bookingRepository, times(1)).deleteById("1");
    }

    @Test
    void testCancelBookingNotFound() {
        // Mock the behavior of the repository when booking is not found
        when(bookingRepository.findById("769")).thenReturn(Optional.empty());

        // Test the service method
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.cancelBooking("769");
        });
        // Verify that deleteById method is not called when booking is not found
        verify(bookingRepository, never()).deleteById("769");
    }
      
}
