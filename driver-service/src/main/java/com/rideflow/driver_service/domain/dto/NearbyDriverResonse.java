package com.rideflow.driver_service.domain.dto;

import com.rideflow.driver_service.geo.GridSpatialIndex;

import java.util.UUID;

public record NearbyDriverResonse (
    UUID driverId,
    double latitude,
    double longitude,
    double distanceKm
) {
    public static NearbyDriverResonse from(GridSpatialIndex.Result result) {
        return new NearbyDriverResonse(result.driverId(), result.latitude(), result.longitude(), result.distanceKm());
    }
}
