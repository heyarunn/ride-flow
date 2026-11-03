package com.rideflow.ride_service.client;

import com.rideflow.ride_service.domain.dto.NearbyDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "driver-service")
public interface DriverServiceClient {

    @GetMapping("/api/v1/drivers/nearby")
    List<NearbyDriver> findNearby(
            @RequestParam("lat") double latitude,
            @RequestParam("long") double longitude,
            @RequestParam("rad") double radiusKm,
            @RequestParam("lim") int limit);
}
