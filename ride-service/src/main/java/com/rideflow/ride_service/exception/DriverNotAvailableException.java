package com.rideflow.ride_service.exception;

public class DriverNotAvailableException extends RuntimeException{
    public DriverNotAvailableException(String message) {
        super(message);
    }
}
