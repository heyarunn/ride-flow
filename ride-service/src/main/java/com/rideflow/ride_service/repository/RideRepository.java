package com.rideflow.ride_service.repository;

import com.rideflow.ride_service.domain.Ride;
import com.rideflow.ride_service.domain.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Query("""
                SELECT r from Ride r
                WHERE (:riderId IS NULL OR r.riderId=:riderId)
                    AND (:driverId IS NULL OR r.driverId=:driverId)
                        AND (:status IS NULL OR r.status=:status)
                ORDER BY r.requestedAt DESC
                """)
    List<Ride> search(@Param("riderId") UUID riderId, @Param("driverId") UUID driverId, @Param("status") RideStatus status);

}
