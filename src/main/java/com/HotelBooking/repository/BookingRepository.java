package com.HotelBooking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.HotelBooking.model.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
	
}
