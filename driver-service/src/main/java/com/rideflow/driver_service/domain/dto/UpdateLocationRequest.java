package com.rideflow.driver_service.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateLocationRequest(
        @NotNull(message = "longitude is missing")
        @DecimalMin(value = "-180.0", message = "longitude must be greater than -180.0")
        @DecimalMax(value = "180.0", message = "longitude must be less than 180.0")
        Double longitude,

        @NotNull(message = "latitude is missing")
        @DecimalMin(value = "-90.0", message = "latitude must be greater than -90.0")
        @DecimalMax(value = "90.0", message = "latitude must be less than 90.0")
        Double latitude
) {
}
