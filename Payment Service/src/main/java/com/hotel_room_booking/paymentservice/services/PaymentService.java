package com.hotel_room_booking.paymentservice.services;


import com.hotel_room_booking.paymentservice.entities.TransactionDetailsEntity;

public interface PaymentService {
    TransactionDetailsEntity performTransaction(TransactionDetailsEntity transactionDetailsEntity);
    TransactionDetailsEntity getTransaction(int id);
}
