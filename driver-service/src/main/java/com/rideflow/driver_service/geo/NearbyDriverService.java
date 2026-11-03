package com.rideflow.driver_service.geo;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NearbyDriverService {

    private static final double CELL_SIZE_DEGREES = 0.05;

    private final DriverRepository driverRepository;

    public NearbyDriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<GridSpatialIndex.Result> findNearByAvailable(double latitude, double longitude, double radiusKm, int limit)
    {
        GridSpatialIndex gridSpatialIndex = new GridSpatialIndex(CELL_SIZE_DEGREES);
        List<Driver> availableDrivers = driverRepository.findByStatus(DriverStatus.AVAILABLE);

        for (Driver driver: availableDrivers) {
            if (driver.getCurrentLatitude()!=null && driver.getCurrentLongitude()!=null) {
                gridSpatialIndex.add(driver.getId(), driver.getCurrentLatitude(), driver.getCurrentLongitude());
            }
        }

        return gridSpatialIndex.queryNearBy(latitude, longitude, radiusKm, limit);
    }
}
