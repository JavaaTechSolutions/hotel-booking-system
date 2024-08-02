package com.jts.hotel.booking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jts.hotel.booking.entity.BookedRoom;
import com.jts.hotel.booking.entity.BookingResponse;
import com.jts.hotel.booking.entity.Room;
import com.jts.hotel.booking.entity.RoomResponse;
import com.jts.hotel.booking.service.BookingService;
import com.jts.hotel.booking.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomController {

	private final RoomService roomService;

	private final BookingService bookingService;

	@PostMapping("/add/new-room")
	public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType, @RequestParam("roomPrice") BigDecimal roomPrice)
			throws SQLException, IOException {

		Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);

		RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(
			@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
			@RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
			@RequestParam("roomType") String roomType) throws SQLException {

		List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
		List<RoomResponse> roomResponses = new ArrayList<>();

		for (Room room : availableRooms) {
			byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
			if (photoBytes != null && photoBytes.length > 0) {
				String photoBase64 = Base64.getEncoder().encodeToString(photoBytes);
				RoomResponse roomResponse = getRoomResponse(room);
				roomResponse.setPhoto(photoBase64);
				roomResponses.add(roomResponse);
			}
		}

		if (roomResponses.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(roomResponses);
	}

	private RoomResponse getRoomResponse(Room room) {
		List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
		List<BookingResponse> bookingInfo = bookings.stream().map(booking -> new BookingResponse(booking.getBookingId(),
				booking.getCheckInDate(), booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();

		byte[] photoBytes = null;
		Blob photoBlob = room.getPhoto();

		if (photoBlob != null) {
			try {
				photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
			} catch (SQLException e) {
				throw new RuntimeException("Error retrieving photo");
			}
		}

		return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes,
				bookingInfo);
	}

	private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingService.getAllBookingsByRoomId(roomId);

	}

}
