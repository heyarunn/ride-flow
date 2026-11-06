package com.rideflow.driver_service.domain.dto;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DriverResponse (
        UUID id,
        UUID authSubjectId,
        String username,
        String email,
        String phoneNumber,
        String vehicleNumber,
        DriverStatus status,
        Double currentLatitude,
        Double currentLongitude,
        LocalDateTime locationUpdatedAt,
        LocalDateTime createdAt
) {
    public static DriverResponse fromDriver(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getAuthSubjectId(),
                driver.getUsername(),
                driver.getEmail(),
                driver.getPhoneNumber(),
                driver.getVehicleNumber(),
                driver.getDriverStatus(),
                driver.getCurrentLatitude(),
                driver.getCurrentLongitude(),
                driver.getLocationUpdatedAt(),
                driver.getCreatedAt()
        );
    }
}
