package com.rideflow.ride_service.exception;

import java.util.UUID;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(UUID id) {
        super("ride not found with id: "+ id);
    }
}
