package com.hotel_room_booking.bookingservice.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hotel_room_booking.bookingservice.dao.BookingDao;
import com.hotel_room_booking.bookingservice.dto.BookingDTO;
import com.hotel_room_booking.bookingservice.dto.TransactionDTO;
import com.hotel_room_booking.bookingservice.entities.BookingInfoEntity;
import com.hotel_room_booking.bookingservice.entities.TransactionInfoEntity;
import com.hotel_room_booking.bookingservice.errorHandler.ErrorHandler;
import com.hotel_room_booking.bookingservice.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/hotel")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingDao bookingDao;

    /*
     * http://127.0.0.1:8081/hotel/booking
     * */
    @PostMapping(
            value = "/booking",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<BookingDTO> bookRoom(@RequestBody BookingDTO bookingDTO) {
        BookingInfoEntity bookingInfoEntity = modelMapper.map(bookingDTO, BookingInfoEntity.class);
        BookingInfoEntity savedBookingInfo = bookingService.acceptDetails(bookingInfoEntity);
        BookingDTO bookingDTO1 = modelMapper.map(savedBookingInfo, BookingDTO.class);
        return new ResponseEntity<>(bookingDTO1, HttpStatus.CREATED);
    }

    /*
     * http://127.0.0.1:8081/hotel/booking/1/transaction
     * */

    @PostMapping(value = "booking/{bookingId}/transaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<Object> bookingConfirmation(@PathVariable(name = "bookingId") int bookingId,
                                                       @RequestBody TransactionDTO transactionDTO) {
        //Verifying the mode of payment is CARD and UPI only
        if (!transactionDTO.getPaymentMode().equals("CARD")
                && !transactionDTO.getPaymentMode().equals("UPI")) {
            System.out.println("Invalid mode of payment");
            ErrorHandler error = new ErrorHandler("Invalid mode of payment", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error,
                    HttpStatus.BAD_REQUEST);
        }

        //Verifying booking exist for the bookingId provided
        Optional<BookingInfoEntity> booking = bookingDao.findById(bookingId);
        if (booking.isEmpty()) {
            System.out.println("Invalid Booking Id");
            ErrorHandler error = new ErrorHandler("Invalid Booking Id", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error,
                    HttpStatus.BAD_REQUEST);
        }

        TransactionInfoEntity transaction = modelMapper.map(transactionDTO, TransactionInfoEntity.class);
        BookingInfoEntity bookingCreated = bookingService.bookRoom(bookingId, transaction);
        BookingDTO bookingDTO = modelMapper.map(bookingCreated, BookingDTO.class);

        return new ResponseEntity<>(bookingDTO, HttpStatus.CREATED);
    }

}
