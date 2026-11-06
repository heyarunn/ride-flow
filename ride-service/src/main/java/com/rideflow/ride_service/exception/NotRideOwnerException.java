package com.rideflow.ride_service.exception;

public class NotRideOwnerException extends RuntimeException {
    public NotRideOwnerException(String message) {
        super(message);
    }
}
