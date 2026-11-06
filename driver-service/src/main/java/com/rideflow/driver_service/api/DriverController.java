package com.rideflow.driver_service.api;

import com.rideflow.driver_service.domain.Driver;
import com.rideflow.driver_service.domain.DriverStatus;
import com.rideflow.driver_service.domain.dto.*;
import com.rideflow.driver_service.geo.NearbyDriverService;
import com.rideflow.driver_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;
    private final NearbyDriverService nearbyDriverService;

    public DriverController(DriverService driverService, NearbyDriverService nearbyDriverService) {
        this.driverService = driverService;
        this.nearbyDriverService = nearbyDriverService;
    }

    @PostMapping
    public ResponseEntity<DriverResponse> registerDriver(@Valid @RequestBody DriverRequest driverRequest) {
        Driver newDriver = driverService.register(driverRequest);
        return new ResponseEntity<>(
                DriverResponse.fromDriver(newDriver),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable UUID id) {
        Driver newDriver = driverService.getById(id);
        return new ResponseEntity<>(
                DriverResponse.fromDriver(newDriver),
                HttpStatus.OK
        );
    }

    @GetMapping
    public List<DriverResponse> getAllDrivers(@RequestParam(required = false)DriverStatus status) {
        return status==null ?
                driverService.getAll().stream().map(DriverResponse::fromDriver).toList():
                driverService.getByStatus(status).stream().map(DriverResponse::fromDriver).toList();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DriverResponse> updateDriverStatus(@PathVariable UUID id,
                                                             @Valid @RequestBody UpdateStatusRequest updateStatusRequest) {
        Driver updateDriver = driverService.updateStatus(id, updateStatusRequest);
        return ResponseEntity.ok(DriverResponse.fromDriver(updateDriver));
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<DriverResponse> updateDriverLocation(@PathVariable UUID id,
                                                               @Valid @RequestBody UpdateLocationRequest updateLocationRequest) {
        Driver updateDriver = driverService.updateLocation(id, updateLocationRequest);
        return new ResponseEntity<>(
                DriverResponse.fromDriver(updateDriver),
                HttpStatus.OK
        );
    }

    @GetMapping("/nearby")
    public List<NearbyDriverResonse> nearBy(
            @RequestParam("lat") double latitude,
            @RequestParam("long") double longitude,
            @RequestParam(name = "rad", defaultValue = "5.0") double radiusKm,
            @RequestParam(name = "lim", defaultValue = "5") int limit) {

        return nearbyDriverService
                .findNearByAvailable(latitude, longitude, radiusKm, limit)
                .stream()
                .map(NearbyDriverResonse::from)
                .toList();
    }
}
