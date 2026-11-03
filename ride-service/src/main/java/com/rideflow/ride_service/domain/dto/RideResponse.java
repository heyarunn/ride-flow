package com.rideflow.ride_service.domain.dto;

import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public record RideResponse (
        UUID id,
        UUID riderId,
        UUID driverId,
        RideStatus rideStatus,
        Double pickupLongitude,
        Double pickupLatitude,
        Double dropoffLongitude,
        Double dropoffLatitude,
        LocalDateTime requestedAt,
        LocalDateTime matchedAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt
) {

    public static RideResponse fromRide(Ride ride) {
        return new RideResponse(
                ride.getId(),
                ride.getRiderId(),
                ride.getDriverId(),
                ride.getStatus(),
                ride.getPickupLongitude(),
                ride.getPickupLongitude(),
                ride.getDropoffLongitude(),
                ride.getDropoffLatitude(),
                ride.getRequestedAt(),
                ride.getMatchedAt(),
                ride.getStartedAt(),
                ride.getCompletedAt(),
                ride.getCancelledAt()
        );
    }
}
