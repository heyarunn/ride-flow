package com.rideflow.ride_service.domain.dto;

import java.util.UUID;

public record NearbyDriver(
        UUID driverId,
       double latitude,
       double longitude) {

}
