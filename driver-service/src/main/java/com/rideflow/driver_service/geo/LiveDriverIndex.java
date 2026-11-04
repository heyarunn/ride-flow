package com.rideflow.driver_service.geo;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.repository.DriverRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class LiveDriverIndex {

    private static final double CELL_SIZE_DEGREES = 0.05;

    private final GridSpatialIndex gridSpatialIndex = new GridSpatialIndex(CELL_SIZE_DEGREES);
    private final DriverRepository driverRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public LiveDriverIndex(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    // Loading the initial index
    @PostConstruct
    @Transactional(readOnly = true)
    public void startUp() {
        int driverCount = 0;
        for (Driver driver: driverRepository.findByStatus(DriverStatus.AVAILABLE)) {
            if (driver.getCurrentLongitude()!=null && driver.getCurrentLatitude()!=null) {
                gridSpatialIndex.upsert(driver.getId(), driver.getCurrentLatitude(), driver.getCurrentLongitude());
                driverCount++;
            }
        }
    }

    // location updation
    public void onLocationUpdatedAsync(UUID driverId, double latitude, double longitude) {
        executorService.submit(() ->{
            gridSpatialIndex.upsert(driverId, latitude, longitude);
        });
    }

    // status updation
    public void onStatusUpdatedAsync(UUID driverId, DriverStatus driverStatus, Double latitude, Double longitude) {
        executorService.submit(() ->{
            if (driverStatus==DriverStatus.AVAILABLE && latitude!=null && longitude!=null) {
                gridSpatialIndex.upsert(driverId, latitude, longitude);
            }else {
                gridSpatialIndex.remove(driverId);
            }
        });
    }

    public List<GridSpatialIndex.Result> findNearby(double latitude, double longitude, double radiusKm, int limit) {
        List<GridSpatialIndex.Result> results = gridSpatialIndex.queryNearBy(latitude, longitude, radiusKm, limit);
        return results;
    }

    public int size() {
        return gridSpatialIndex.size();
    }

    // Clear executor service before destroy
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
