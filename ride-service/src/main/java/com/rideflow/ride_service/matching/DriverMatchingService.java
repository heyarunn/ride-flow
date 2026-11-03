package com.rideflow.ride_service.matching;

import com.rideflow.ride_service.domain.Ride;

import java.util.Optional;
import java.util.UUID;

public interface DriverMatchingService {
    Optional<UUID> findDriverFor(Double pickupLatitude, Double pickupLongitude);
}
