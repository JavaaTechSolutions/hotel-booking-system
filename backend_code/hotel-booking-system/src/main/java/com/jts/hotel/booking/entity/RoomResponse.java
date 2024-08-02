package com.jts.hotel.booking.entity;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomResponse {
	private Long id;
	private String roomType;
	private BigDecimal roomPrice;
	private boolean isBooked;
	private String photo;
	private String hotelName;
	private String details;
	private List<BookingResponse> bookings;

	public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}

	public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoBytes,
			List<BookingResponse> bookings) {
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
		this.bookings = bookings;
	}

}
