package com.hotel_room_booking.bookingservice.dao;

import com.hotel_room_booking.bookingservice.entities.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<BookingInfoEntity,Integer> {
}
