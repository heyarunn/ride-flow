package com.rideflow.driver_service.service.impl;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.domain.dto.DriverRequest;
import com.rideflow.driver_service.domain.dto.UpdateLocationRequest;
import com.rideflow.driver_service.domain.dto.UpdateStatusRequest;
import com.rideflow.driver_service.exception.DriverNotFoundException;
import com.rideflow.driver_service.exception.DuplicateDriverException;
import com.rideflow.driver_service.repository.DriverRepository;
import com.rideflow.driver_service.service.DriverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver register(DriverRequest driverRequest) {
        if (driverRepository.existsByEmail(driverRequest.email())) {
            throw new DuplicateDriverException("email already registered: "+driverRequest.email());
        }

        if (driverRepository.existsByPhoneNumber(driverRequest.phoneNumber())) {
            throw new DuplicateDriverException("phone number already registered: "+driverRequest.phoneNumber());
        }

        if (driverRepository.existsByVehicleNumber(driverRequest.vehicleNumber())) {
            throw new DuplicateDriverException("vehicle number already registered: "+driverRequest.vehicleNumber());
        }

        Driver driver = new Driver(driverRequest.username(), driverRequest.email(), driverRequest.phoneNumber(), driverRequest.vehicleNumber());
        return driverRepository.save(driver);
    }

    @Override
    @Transactional(readOnly = true)
    public Driver getById(UUID id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> getByStatus(DriverStatus status) {
        return driverRepository.findByStatus(status);
    }

    @Override
    public Driver updateStatus(UUID id, UpdateStatusRequest updateStatusRequest) {
        Driver currDriver = getById(id);
        currDriver.setDriverStatus(updateStatusRequest.status());
        return currDriver;
    }

    @Override
    public Driver updateLocation(UUID id, UpdateLocationRequest updateLocationRequest) {
        Driver currDriver = getById(id);
        currDriver.updateLocation(updateLocationRequest.longitude(), updateLocationRequest.latitude());
        return currDriver;
    }

}
