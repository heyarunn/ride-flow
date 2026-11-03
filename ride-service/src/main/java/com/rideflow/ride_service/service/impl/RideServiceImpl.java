package com.rideflow.ride_service.service.impl;

import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import com.rideflow.ride_service.domain.dto.RideRequest;
import com.rideflow.ride_service.exception.DriverNotAvailableException;
import com.rideflow.ride_service.exception.RideNotFoundException;
import com.rideflow.ride_service.matching.GeoDriverMatchingService;
import com.rideflow.ride_service.repository.RideRepository;
import com.rideflow.ride_service.service.RideService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final GeoDriverMatchingService driverMatchingService;

    public RideServiceImpl(RideRepository rideRepository, GeoDriverMatchingService driverMatchingService) {
        this.rideRepository = rideRepository;
        this.driverMatchingService = driverMatchingService;
    }

    @Override
    public Ride requestRide(RideRequest rideRequest) {
        Ride nwRide = new Ride(rideRequest.riderId(), rideRequest.pickupLatitude(),
                rideRequest.pickupLongitude(), rideRequest.dropoffLatitude(), rideRequest.dropoffLongitude());

        UUID driverId = driverMatchingService.findDriverFor(rideRequest.pickupLatitude(), rideRequest.pickupLongitude())
                .orElseThrow(() -> new DriverNotAvailableException("No drivers available for the selected location"));
        nwRide.matchDriver(driverId);
        return rideRepository.save(nwRide);
    }

    @Override
    @Transactional(readOnly = true)
    public Ride getRideById(UUID id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
    }

    @Override
    public List<Ride> search(UUID riderId, UUID driverId, RideStatus status) {
        return rideRepository.search(riderId, driverId, status);
    }

    @Override
    public Ride markDriverArrived(UUID id) {
        Ride currRide = getRideById(id);
        currRide.markDriverArrived();
        return currRide;
    }

    @Override
    public Ride rideStarted(UUID id) {
        Ride currRide = getRideById(id);
        currRide.rideStarted();
        return currRide;
    }

    @Override
    public Ride rideCompleted(UUID id) {
        Ride currRide = getRideById(id);
        currRide.rideCompleted();
        return currRide;
    }

    @Override
    public Ride rideCancelled(UUID id) {
        Ride currRide = getRideById(id);
        currRide.rideCancelled();
        return currRide;
    }
}
