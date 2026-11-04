
package com.rideflow.driver_service.geo;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.repository.DriverRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class LiveDriverIndex {

    private static final String GEO_KEY = "rideflow:drivers:available";

    private final DriverRepository driverRepository;
    private final StringRedisTemplate redisTemplate;
    private final GeoOperations<String, String> geo;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public LiveDriverIndex(DriverRepository driverRepository, StringRedisTemplate redisTemplate) {
        this.driverRepository = driverRepository;
        this.redisTemplate = redisTemplate;
        this.geo = redisTemplate.opsForGeo();
    }

    // Loading the initial index
    @PostConstruct
    @Transactional(readOnly = true)
    public void startUp() {
        try {
            redisTemplate.delete(GEO_KEY);
            int driverCount = 0;

            for (Driver driver : driverRepository.findByStatus(DriverStatus.AVAILABLE)) {
                if (driver.getCurrentLongitude() != null && driver.getCurrentLatitude() != null) {
                    upsert(driver.getId(), driver.getCurrentLatitude(), driver.getCurrentLongitude());
                    driverCount++;
                }
            }

        } catch (Exception e) {
        }
    }

    // insert a driver or move a driver
    public void upsert(UUID driverId, double latitude, double longitude) {
        validateCoordinates(latitude, longitude);
        geo.add(GEO_KEY, new Point(longitude, latitude), driverId.toString());
    }

    // remove a driver out of the matchable set
    public void remove(UUID driverId) {
        geo.remove(GEO_KEY, driverId.toString());
    }

    // location updation
    public void onLocationUpdatedAsync(UUID driverId, double latitude, double longitude) {
        executorService.submit(() -> {
            try {
                upsert(driverId, latitude, longitude);
            } catch (Exception e) {
            }
        });
    }

    // status updation
    public void onStatusUpdatedAsync(UUID driverId, DriverStatus driverStatus, Double latitude, Double longitude) {
        executorService.submit(() -> {
            try {
                if (driverStatus == DriverStatus.AVAILABLE && latitude != null && longitude != null) {
                    upsert(driverId, latitude, longitude);
                } else {
                    remove(driverId);
                }
            } catch (Exception e) {
            }
        });
    }

    public List<GridSpatialIndex.Result> findNearby(double latitude, double longitude, double radiusKm, int limit) {

        validateCoordinates(latitude, longitude);

        if (radiusKm <= 0 || limit <= 0) {
            throw new IllegalArgumentException("Radius and limit must be positive");
        }

        GeoResults<GeoLocation<String>> available = geo.search(
                GEO_KEY,
                GeoReference.fromCoordinate(longitude, latitude),
                new Distance(radiusKm, Metrics.KILOMETERS),
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                        .includeDistance()
                        .includeCoordinates()
                        .sortAscending()
                        .limit(limit));

        if (available == null || available.getContent().isEmpty()) {
            return List.of();
        }

        List<GridSpatialIndex.Result> results = new ArrayList<>();

        for (var result : available) {
            GeoLocation<String> location = result.getContent();
            Point point = location.getPoint();

            results.add(new GridSpatialIndex.Result(
                    UUID.fromString(location.getName()),
                    point.getY(),
                    point.getX(),
                    result.getDistance().getValue()
            ));
        }

        return results;
    }

    private void validateCoordinates(double latitude, double longitude) {
        if (!Double.isFinite(latitude) ||
                !Double.isFinite(longitude) ||
                latitude < -90 || latitude > 90 ||
                longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid latitude or longitude");
        }
    }

    // Clear executor service before destroy
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
