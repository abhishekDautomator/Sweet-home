package com.hotel_room_booking.paymentservice.services;

import com.hotel_room_booking.paymentservice.dao.TransactionDAO;
import com.hotel_room_booking.paymentservice.entities.TransactionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    TransactionDAO transactionDAO;

    public TransactionDetailsEntity performTransaction(TransactionDetailsEntity transactionDetailsEntity){
        return transactionDAO.save(transactionDetailsEntity);
    }

    public TransactionDetailsEntity getTransaction(int id){
        return transactionDAO.getById(id);
    }
}
