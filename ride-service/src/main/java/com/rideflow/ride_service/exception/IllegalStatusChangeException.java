package com.rideflow.ride_service.exception;

import com.rideflow.ride_service.domain.RideStatus;

public class IllegalStatusChangeException extends RuntimeException {
    public IllegalStatusChangeException(RideStatus from, RideStatus to) {
        super("Illegeal status change from: "+from+" to: "+to);
    }
}
