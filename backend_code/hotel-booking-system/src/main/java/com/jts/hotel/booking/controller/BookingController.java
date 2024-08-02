package com.jts.hotel.booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jts.hotel.booking.entity.BookedRoom;
import com.jts.hotel.booking.service.BookingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

	private final BookingService bookingService;

	@PostMapping("/room/booking")
	public ResponseEntity<String> saveBooking(@RequestParam Long roomId, @RequestBody BookedRoom bookingRequest) {
		String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);

		return ResponseEntity.ok("Room booked successfully, Your booking confirmation code is :" + confirmationCode);
	}

}
