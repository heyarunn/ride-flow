package com.rideflow.ride_service.domain.dto;

import java.util.UUID;

public record DriverResponse(
        UUID id,
        UUID authSubjectId
) {
}
