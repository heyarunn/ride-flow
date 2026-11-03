package com.rideflow.driver_service.exception;

import java.util.UUID;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(UUID id) {
        super("Driver not found with id: "+id);
    }
}
