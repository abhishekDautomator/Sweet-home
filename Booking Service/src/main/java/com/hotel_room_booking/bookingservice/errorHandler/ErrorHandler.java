package com.hotel_room_booking.bookingservice.errorHandler;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorHandler{
    private String message;
    private Object statusCode;

    public ErrorHandler(String errorMessage, int value) {
        this.message = errorMessage;
        this.statusCode = value;
    }
}
