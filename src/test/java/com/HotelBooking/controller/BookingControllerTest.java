package com.HotelBooking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.HotelBooking.exception.BookingNotFoundException;
import com.HotelBooking.model.Booking;
import com.HotelBooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ControllerTest class for Testing the httpMethods(GET,PUT,POST,DELETE) hotel booking system.
 */

@WebMvcTest
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private Booking sampleBooking;
    private List<Booking> sampleBookingList;

    @BeforeEach
    void setUp() {
        sampleBooking = new Booking();
        sampleBooking.setId("65b14a136023b32927f1cfbe");
        sampleBooking.setGuestName("raja");
        sampleBooking.setCheckIn(LocalDateTime.parse("2024-02-01T00:00:00"));
        sampleBooking.setCheckOut(LocalDateTime.parse("2024-02-05T00:00:00"));

        sampleBookingList = new ArrayList<>();
        sampleBookingList.add(sampleBooking);

        Mockito.when(bookingService.getAllBookings()).thenReturn(sampleBookingList);
        Mockito.when(bookingService.getBookingById("65b14a136023b32927f1cfbe")).thenReturn(sampleBooking);
        Mockito.when(bookingService.getBookingById("nonexistent-id")).thenThrow(new BookingNotFoundException("Booking not found with ID: nonexistent-id"));
        Mockito.when(bookingService.createBooking(Mockito.any(Booking.class))).thenReturn(sampleBooking);
        Mockito.doNothing().when(bookingService).cancelBooking("65b14a136023b32927f1cfbe");
        Mockito.doThrow(BookingNotFoundException.class).when(bookingService).cancelBooking("nonexistent-id");
        Mockito.when(bookingService.updateBooking(Mockito.eq("65b14a136023b32927f1cfbe"), Mockito.any(Booking.class))).thenReturn(sampleBooking);
        Mockito.when(bookingService.updateBooking(Mockito.eq("nonexistent-id"), Mockito.any(Booking.class))).thenThrow(new BookingNotFoundException("Booking not found with ID: nonexistent-id"));
    }

    @Test
    void getAllBookings() throws Exception {
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("65b14a136023b32927f1cfbe"))
                .andExpect(jsonPath("$[0].guestName").value("raja"))
                .andExpect(jsonPath("$[0].checkIn").value("2024-02-01T00:00:00"))
                .andExpect(jsonPath("$[0].checkOut").value("2024-02-05T00:00:00"));
    }

    @Test
    void getBookingById() throws Exception {
        mockMvc.perform(get("/api/bookings/{id}", "65b14a136023b32927f1cfbe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("65b14a136023b32927f1cfbe"))
                .andExpect(jsonPath("$.guestName").value("raja"))
                .andExpect(jsonPath("$.checkIn").value("2024-02-01T00:00:00"))
                .andExpect(jsonPath("$.checkOut").value("2024-02-05T00:00:00"));
    }

    @Test
    void createBooking() throws Exception {
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBooking)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("65b14a136023b32927f1cfbe"))
                .andExpect(jsonPath("$.guestName").value("raja"))
                .andExpect(jsonPath("$.checkIn").value("2024-02-01T00:00:00"))
                .andExpect(jsonPath("$.checkOut").value("2024-02-05T00:00:00"));
    }

    @Test
    void cancelBooking() throws Exception {
        mockMvc.perform(delete("/api/bookings/{id}", "65b14a136023b32927f1cfbe"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelBookingNotFound() throws Exception {
        mockMvc.perform(delete("/api/bookings/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBooking() throws Exception {
        mockMvc.perform(put("/api/bookings/{id}", "65b14a136023b32927f1cfbe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBooking)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("65b14a136023b32927f1cfbe"))
                .andExpect(jsonPath("$.guestName").value("raja"))
                .andExpect(jsonPath("$.checkIn").value("2024-02-01T00:00:00"))
                .andExpect(jsonPath("$.checkOut").value("2024-02-05T00:00:00"));
    }

}