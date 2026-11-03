package com.rideflow.ride_service.api;

import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import com.rideflow.ride_service.domain.dto.RideRequest;
import com.rideflow.ride_service.domain.dto.RideResponse;
import com.rideflow.ride_service.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    private ResponseEntity<RideResponse> requestRide(@Valid @RequestBody RideRequest rideRequest) {
        Ride nwRide = rideService.requestRide(rideRequest);
        return new ResponseEntity<>(
                RideResponse.fromRide(nwRide),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable UUID id) {
        Ride currRide = rideService.getRideById(id);
        return new ResponseEntity<>(
                RideResponse.fromRide(currRide),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<RideResponse> search(@RequestParam(required = false) UUID riderId,
                                     @RequestParam(required = false) UUID driverId,
                                     @RequestParam(required = false) RideStatus status) {
        return rideService.search(riderId, driverId, status)
                .stream().map(RideResponse::fromRide).toList();
    }

    // Update status apis
    @PutMapping("/{id}/arrived")
    public ResponseEntity<RideResponse> markDriverArrived(@PathVariable UUID id) {
        Ride updatedRide = rideService.markDriverArrived(id);
        return new ResponseEntity<>(
                RideResponse.fromRide(updatedRide),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponse> rideStarted(@PathVariable UUID id) {
        Ride updatedRide = rideService.markDriverArrived(id);
        return new ResponseEntity<>(
                RideResponse.fromRide(updatedRide),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<RideResponse> rideCompleted(@PathVariable UUID id) {
        Ride updatedRide = rideService.markDriverArrived(id);
        return new ResponseEntity<>(
                RideResponse.fromRide(updatedRide),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponse> rideCancelled(@PathVariable UUID id) {
        Ride updatedRide = rideService.markDriverArrived(id);
        return new ResponseEntity<>(
                RideResponse.fromRide(updatedRide),
                HttpStatus.OK
        );
    }
}
