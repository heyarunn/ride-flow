package com.rideflow.ride_service.matching;

import com.rideflow.ride_service.client.DriverServiceClient;
import com.rideflow.ride_service.domain.dto.NearbyDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GeoDriverMatchingService implements DriverMatchingService {

    // private static final UUID SAMPLE_DRIVER_ID = UUID.fromString("650e8400-e29b-41d4-a716-446655440002");
    private final DriverServiceClient driverServiceClient;
    private final double searchRadius;

    public GeoDriverMatchingService(DriverServiceClient driverServiceClient,
                    @Value("${rideflow.matching.radius-km:5.0}") double searchRadius) {
        this.driverServiceClient = driverServiceClient;
        this.searchRadius = searchRadius;
    }

    @Override
    public Optional<UUID> findDriverFor(Double pickupLatitude, Double pickupLongitude) {
        // Driver matching business logic
        // return Optional.of(SAMPLE_DRIVER_ID);
        List<NearbyDriver> nearbyDriverList = driverServiceClient.findNearby(pickupLatitude, pickupLongitude, searchRadius, 5);
        if (nearbyDriverList==null || nearbyDriverList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(nearbyDriverList.getFirst().driverId());
    }
}
