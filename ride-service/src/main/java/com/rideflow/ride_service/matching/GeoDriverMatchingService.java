package com.rideflow.ride_service.matching;

import com.rideflow.ride_service.client.DriverServiceClient;
import com.rideflow.ride_service.domain.dto.NearbyDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Score;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Driver;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GeoDriverMatchingService implements DriverMatchingService {

    private record ScoredDriver(
            NearbyDriver driver,
            double score
    ) {

    }

    // Best driver first
    private static final Comparator<ScoredDriver> bestAssignee = Comparator
            .comparingDouble(ScoredDriver::score).reversed()
            .thenComparingDouble(scored -> scored.driver().distKm());

    // private static final UUID SAMPLE_DRIVER_ID = UUID.fromString("650e8400-e29b-41d4-a716-446655440002");
    private final DriverServiceClient driverServiceClient;
    private final DriverScoringService driverScoringService;
    private final double searchRadius;
    private final int driverLimit;

    public GeoDriverMatchingService(DriverServiceClient driverServiceClient,
                                    DriverScoringService driverScoringService,
                                    @Value("${rideflow.matching.radius-km:5.0}") double searchRadius,
                                    @Value("${rideflow.matching.driver-limit:10}") int driverCount) {
        this.driverServiceClient = driverServiceClient;
        this.driverScoringService = driverScoringService;
        this.searchRadius = searchRadius;
        this.driverLimit = driverCount;
    }

    @Override
    public Optional<UUID> findDriverFor(Double pickupLatitude, Double pickupLongitude) {
        // Driver matching business logic
        // return Optional.of(SAMPLE_DRIVER_ID);
        List<NearbyDriver> nearbyDriverList;
        try {
            nearbyDriverList = driverServiceClient
                    .findNearby(pickupLatitude, pickupLongitude, searchRadius, driverLimit);
        }
        catch (Exception ex) {
            throw ex;
        }

        if (nearbyDriverList==null || nearbyDriverList.isEmpty()) {
            return Optional.empty();
        }

        LocalDateTime now = LocalDateTime.now();
        PriorityQueue<ScoredDriver> heap = new PriorityQueue<>();

        for (NearbyDriver nearbyDriver: nearbyDriverList) {
            double currScore = driverScoringService.scoreDriver(nearbyDriver, searchRadius, now);
            heap.add(new ScoredDriver(nearbyDriver, nearbyDriver.distKm()));
        }

        ScoredDriver bestDriverForTrip = heap.poll();

        assert bestDriverForTrip != null;
        return Optional.of(bestDriverForTrip.driver().driverId());
    }
}
