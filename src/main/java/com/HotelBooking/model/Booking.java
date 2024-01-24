package com.HotelBooking.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a booking entity in the hotel booking system.
 */

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    
    @NotBlank(message = "Guest name must not be blank")
    @Size(max = 255, message = "Guest name must not exceed 255 characters")
    private String guestName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	public LocalDateTime getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(LocalDateTime checkIn) {
		this.checkIn = checkIn;
	}
	public LocalDateTime getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(LocalDateTime checkOut) {
		this.checkOut = checkOut;
	}
    
}
