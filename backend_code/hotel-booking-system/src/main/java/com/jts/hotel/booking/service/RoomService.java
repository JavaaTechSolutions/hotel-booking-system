package com.jts.hotel.booking.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jts.hotel.booking.entity.Room;
import com.jts.hotel.booking.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	
	private final RoomRepository roomRepository;

	public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
		Room room = new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);

		if (!file.isEmpty()) {
			byte[] photoBytes = file.getBytes();
			Blob photoBlob = new SerialBlob(photoBytes);
			room.setPhoto(photoBlob);
		}

		return roomRepository.save(room);
	}
	
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}
	
	public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
		return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
	}
	
	public Optional<Room> getRoomById(Long roomId) {
		return Optional.of(roomRepository.findById(roomId).get());
	}
	
	public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
		Optional<Room> theRoom = roomRepository.findById(roomId);

		if (theRoom.isEmpty()) {
			throw new RuntimeException("Sorry, Room not found!");
		}

		Blob photoBlob = theRoom.get().getPhoto();

		if (photoBlob != null) {
			return photoBlob.getBytes(1, (int) photoBlob.length());
		}

		return null;
	}
}


