package com.hotel_room_booking.bookingservice.services;

import com.hotel_room_booking.bookingservice.entities.BookingInfoEntity;
import com.hotel_room_booking.bookingservice.entities.TransactionInfoEntity;

public interface BookingService {

    BookingInfoEntity acceptDetails(BookingInfoEntity bookingInfoEntity);
    BookingInfoEntity bookRoom(int bookingId, TransactionInfoEntity transactionInfoEntity);
}
