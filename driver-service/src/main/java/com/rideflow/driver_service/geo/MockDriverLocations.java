package com.rideflow.driver_service.geo;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockDriverLocations {
    private static final double MAX_MOVE_DEGREES = 0.0004;

    private final DriverRepository driverRepository;
    private final LiveDriverIndex liveDriverIndex;
    private final boolean enabled;

    public MockDriverLocations(DriverRepository driverRepository,
                               LiveDriverIndex liveDriverIndex,
                               @Value("${rideflow.locationmock.enabled:false}") boolean enabled) {
        this.driverRepository = driverRepository;
        this.liveDriverIndex = liveDriverIndex;
        this.enabled = enabled;
    }

    @Scheduled(fixedDelayString = "${rideflow.locationmock.interval-ms:3000}")
    @Transactional
    public void updateLiveLocation() {
        if (!enabled) {
            return;
        }

        List<Driver> availableDrivers = driverRepository.findByStatus(DriverStatus.AVAILABLE);
        int movedDrivers = 0;
        for (Driver driver: availableDrivers) {
            if (driver.getCurrentLatitude()==null && driver.getCurrentLongitude()==null) {
                continue;
            }

            double newLat = driver.getCurrentLatitude()+randomMove();
            double newLong = driver.getCurrentLongitude()+randomMove();

            driver.updateLocation(newLat, newLong);
            liveDriverIndex.onLocationUpdatedAsync(driver.getId(), newLat, newLong);
            movedDrivers++;
        }

    }

    // mocking movement in degrees
    private double randomMove() {
        return ThreadLocalRandom.current().nextDouble(-MAX_MOVE_DEGREES, MAX_MOVE_DEGREES);
    }
}
