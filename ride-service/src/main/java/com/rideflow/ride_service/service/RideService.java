package com.rideflow.ride_service.service;

import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import com.rideflow.ride_service.domain.dto.RideRequest;

import java.util.List;
import java.util.UUID;

public interface RideService {

    Ride requestRide(RideRequest rideRequest);

    Ride getRideById(UUID id);

    List<Ride> search(UUID riderId, UUID driverId, RideStatus status);

    Ride markDriverArrived(UUID id);

    Ride rideStarted(UUID id);

    Ride rideCompleted(UUID id);

    Ride rideCancelled(UUID id);

}
