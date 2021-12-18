package com.hotel_room_booking.bookingservice.services;

import com.hotel_room_booking.bookingservice.dao.BookingDao;
import com.hotel_room_booking.bookingservice.entities.BookingInfoEntity;
import com.hotel_room_booking.bookingservice.entities.TransactionInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Value("${paymentApp.url}")
    private String paymentAppUrl;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BookingInfoEntity acceptDetails(BookingInfoEntity bookingInfoEntity) {

        //Generating room numbers
        int numOfRooms = bookingInfoEntity.getNumOfRooms();
        Random random = new Random();
        List<Integer> roomNumbers = random
                .ints(0, 100)
                .distinct()
                .limit(numOfRooms)
                .boxed()
                .collect(Collectors.toList());
        StringBuilder roomNumbersAsString = new StringBuilder();
        for (int n :
                roomNumbers) {
            roomNumbersAsString.append(n).append(",");
        }
        bookingInfoEntity.setRoomNumbers(roomNumbersAsString
                .deleteCharAt(roomNumbersAsString.length() - 1).toString());

        //Calculating booking price
        int basePrice = 1000;
        int numOfDays = (int) ChronoUnit.DAYS.between(bookingInfoEntity.getFromDate().toInstant(),
                bookingInfoEntity.getToDate().toInstant());
        System.out.println("Number of days of visit : " + numOfDays);
        int roomPrice = basePrice * numOfRooms * numOfDays;
        System.out.println("Room price : " + roomPrice);
        bookingInfoEntity.setRoomPrice(roomPrice);

        //Setting current time
        bookingInfoEntity.setBookedOn(new Date());

        return bookingDao.save(bookingInfoEntity);
    }

    @Override
    public BookingInfoEntity bookRoom(int bookingId,
                                      TransactionInfoEntity transactionInfoEntity) {

        Optional<BookingInfoEntity> bookingCreated = bookingDao.findById(bookingId);

        //Setting bookingId for a transaction
        transactionInfoEntity.setBookingId(bookingId);

        System.out.println("Transaction entity after setting booking id : " + transactionInfoEntity);
        System.out.println("Payment mode sent : " + transactionInfoEntity.getPaymentMode());
        int transactionIdCreated = restTemplate.postForObject(paymentAppUrl, transactionInfoEntity, Integer.class);

        //Setting transactionId for the booking created
        bookingCreated.get().setTransactionId(transactionIdCreated);

        String message = "Booking confirmed for user with aadhaar number: "
                + bookingCreated.get().getAadharNumber()
                + "    |    "
                + "Here are the booking details:    " + bookingCreated.get();
        System.out.println(message);

        return bookingDao.save(bookingCreated.get());
    }

}
