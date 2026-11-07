package com.rideflow.ride_service.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NearbyDriver(
    UUID driverId,
    double latitude,
    double longitude,
    double distKm,
    Double rating,
    LocalDateTime availableSince
) {

}
