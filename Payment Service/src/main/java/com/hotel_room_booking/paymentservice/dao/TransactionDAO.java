package com.hotel_room_booking.paymentservice.dao;

import com.hotel_room_booking.paymentservice.entities.TransactionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDAO extends JpaRepository<TransactionDetailsEntity,Integer> {
}
