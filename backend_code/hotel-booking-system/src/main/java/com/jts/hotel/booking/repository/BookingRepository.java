package com.jts.hotel.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jts.hotel.booking.entity.BookedRoom;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

	List<BookedRoom> findByRoomId(Long roomId);
}
