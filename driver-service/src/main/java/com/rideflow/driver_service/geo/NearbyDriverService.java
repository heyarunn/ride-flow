package com.rideflow.driver_service.geo;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NearbyDriverService {

    private final LiveDriverIndex liveDriverIndex;

    public NearbyDriverService(LiveDriverIndex liveDriverIndex) {
        this.liveDriverIndex = liveDriverIndex;
    }

    // Find AVAILABLE drivers from nearby with in radiusKm
    public List<GridSpatialIndex.Result> findNearByAvailable(double latitude, double longitude, double radiusKm, int limit)
    {
        return liveDriverIndex.findNearby(latitude, longitude, radiusKm, limit);
    }
}
