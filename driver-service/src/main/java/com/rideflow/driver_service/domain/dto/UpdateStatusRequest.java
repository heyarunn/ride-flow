package com.rideflow.driver_service.domain.dto;

import com.rideflow.driver_service.domain.DriverStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "driver status is required")
        DriverStatus status
) {
}
