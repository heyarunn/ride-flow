package com.rideflow.driver_service.domain.dto;

import com.rideflow.driver_service.geo.GridSpatialIndex;

import java.time.LocalDateTime;
import java.util.UUID;

public record NearbyDriverResonse (
    UUID driverId,
    double latitude,
    double longitude,
    double distanceKm,
    double rating,
    LocalDateTime availableSince
) {
    public static NearbyDriverResonse from(GridSpatialIndex.Result result) {
        return new NearbyDriverResonse(result.driverId(), result.latitude(), result.longitude(), result.distanceKm(), result.rating(), result.availableSince());
    }
}
