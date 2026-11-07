package com.rideflow.ride_service.service.impl;

import com.rideflow.common_events.event.*;
import com.rideflow.common_security.CurrentUser;
import com.rideflow.ride_service.client.DriverServiceClient;
import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import com.rideflow.ride_service.domain.dto.DriverResponse;
import com.rideflow.ride_service.domain.dto.RideRequest;
import com.rideflow.ride_service.exception.DriverNotAvailableException;
import com.rideflow.ride_service.exception.NotRideOwnerException;
import com.rideflow.ride_service.exception.RideNotFoundException;
import com.rideflow.ride_service.matching.GeoDriverMatchingService;
import com.rideflow.ride_service.repository.RideRepository;
import com.rideflow.ride_service.service.RideService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RideServiceImpl implements RideService {
    private static final String CANCEL_REASON = "CANCELLED_THROUGH_API";

    private final RideRepository rideRepository;
    private final GeoDriverMatchingService driverMatchingService;
    private final ApplicationEventPublisher eventPublisher;
    private final DriverServiceClient driverServiceClient;

    public RideServiceImpl(RideRepository rideRepository, GeoDriverMatchingService driverMatchingService, ApplicationEventPublisher eventPublisher, DriverServiceClient driverServiceClient) {
        this.rideRepository = rideRepository;
        this.driverMatchingService = driverMatchingService;
        this.eventPublisher = eventPublisher;
        this.driverServiceClient = driverServiceClient;
    }

    @Override
    public Ride requestRide(RideRequest rideRequest) {
        Ride nwRide = new Ride(rideRequest.riderId(), rideRequest.pickupLatitude(),
                rideRequest.pickupLongitude(), rideRequest.dropoffLatitude(), rideRequest.dropoffLongitude());

        UUID driverId = driverMatchingService.findDriverFor(rideRequest.pickupLatitude(), rideRequest.pickupLongitude())
                .orElseThrow(() -> new DriverNotAvailableException("No drivers available for the selected location"));
        nwRide.matchDriver(driverId);
        Ride saved = rideRepository.save(nwRide);
        eventPublisher.publishEvent(RideRequestedEvent.of(saved.getId(), saved.getRiderId(),
                saved.getPickupLatitude(), saved.getPickupLongitude(),
                saved.getDropoffLatitude(), saved.getDropoffLongitude()));
        // Matching has happend in the above method call
        eventPublisher.publishEvent(RideMatchedEvent.of(saved.getId(), saved.getRiderId(), saved.getDriverId()));
        return saved;
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
        assetRideOwnership(currRide);
        currRide.markDriverArrived();
        return currRide;
    }

    @Override
    public Ride rideStarted(UUID id) {
        Ride currRide = getRideById(id);
        assetRideOwnership(currRide);
        currRide.rideStarted();
        eventPublisher.publishEvent(RideStartedEvent.of(id, currRide.getRiderId(), currRide.getDriverId()));
        return currRide;
    }

    @Override
    public Ride rideCompleted(UUID id) {
        Ride currRide = getRideById(id);
        assetRideOwnership(currRide);
        currRide.rideCompleted();
        eventPublisher.publishEvent(RideCompletedEvent.of(id, currRide.getRiderId(), currRide.getDriverId()));
        return currRide;
    }

    @Override
    public Ride rideCancelled(UUID id) {
        Ride currRide = getRideById(id);
        currRide.rideCancelled();
        eventPublisher.publishEvent(RideCancelledEvent.of(id, currRide.getRiderId(), currRide.getDriverId(), CANCEL_REASON));
        return currRide;
    }

    @Override
    public void assetRideOwnership(Ride ride) {
        UUID caller = CurrentUser.subjectId()
                .orElseThrow(() -> new NotRideOwnerException("user is not authenticated"));
        if (ride.getDriverId()==null)
            throw new NotRideOwnerException("ride has no assigned driver");
        DriverResponse driverResponse = driverServiceClient.getDriverById(ride.getDriverId());
        if (driverResponse.authSubjectId()==null || !driverResponse.authSubjectId().equals(caller)) {
            throw new NotRideOwnerException("loggedin user is not the assigned driver for the ride: "+ ride.getId());
        }
    }
}
