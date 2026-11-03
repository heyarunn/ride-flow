package com.rideflow.driver_service.domain.dto;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;

import java.util.UUID;

public record DriverResponse (
        UUID id,
        String username,
        String email,
        String phoneNumber,
        String vehicleNumber,
        DriverStatus status
) {
    public static DriverResponse fromDriver(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUsername(),
                driver.getEmail(),
                driver.getPhoneNumber(),
                driver.getVehicleNumber(),
                driver.getDriverStatus()
        );
    }
}
