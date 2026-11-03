package com.rideflow.ride_service.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RideRequest (
        @NotNull(message = "rider id is required")
        UUID riderId,

        @NotNull(message = "pickup longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude/latitude value must be greater than -180.0")
        @DecimalMax(value = "180.0", message = "longitude/latitude value must be less than 180.0")
        Double pickupLongitude,

        @NotNull(message = "pickup latitude is required")
        @DecimalMin(value = "-90.0", message = "longitude/latitude value must be greater than -90.0")
        @DecimalMax(value = "90.0", message = "longitude/latitude value must be less than 90.0")
        Double pickupLatitude,

        @NotNull(message = "dropoff longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude/latitude value must be greater than -180.0")
        @DecimalMax(value = "180.0", message = "longitude/latitude value must be less than 180.0")
        Double dropoffLongitude,

        @NotNull(message = "dropoff latitude is required")
        @DecimalMin(value = "-90.0", message = "longitude/latitude value must be greater than -90.0")
        @DecimalMax(value = "90.0", message = "longitude/latitude value must be less than 90.0")
        Double dropoffLatitude
) {
}
