package com.rideflow.driver_service.service;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.domain.dto.DriverRequest;
import com.rideflow.driver_service.domain.dto.UpdateLocationRequest;
import com.rideflow.driver_service.domain.dto.UpdateStatusRequest;

import java.util.List;
import java.util.UUID;

public interface DriverService {

    Driver register(DriverRequest driverRequest);

    Driver getById(UUID id);

    List<Driver> getAll();

    List<Driver> getByStatus(DriverStatus status);

    Driver updateStatus(UUID id, UpdateStatusRequest updateStatusRequest);

    Driver updateLocation(UUID id, UpdateLocationRequest updateLocationRequest);

}
