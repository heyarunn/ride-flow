package com.rideflow.ride_service.domain;

import com.rideflow.ride_service.exception.IllegalStatusChangeException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rider_id", nullable = false)
    private UUID riderId;

    @Column(name = "driver_id")
    private UUID driverId;

    @Enumerated(value = EnumType.STRING)
    @Column(name="status", nullable = false)
    private RideStatus status;

    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;

    @Column(name = "dropoff_latitude", nullable = false)
    private Double dropoffLatitude;

    @Column(name = "dropoff_longitude", nullable = false)
    private Double dropoffLongitude;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "matched_at")
    private LocalDateTime matchedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Ride() {
    }

    public Ride(UUID riderId, Double pickupLatitude, Double pickupLongitude, Double dropoffLatitude, Double dropoffLongitude) {
        this.riderId = riderId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.dropoffLatitude = dropoffLatitude;
        this.dropoffLongitude = dropoffLongitude;
        this.status = RideStatus.REQUESTED;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.requestedAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Driver matched
    public void matchDriver(UUID driverId) {
        transitionTo(RideStatus.MATCHED);
        this.driverId = driverId;
        this.matchedAt = LocalDateTime.now();
    }
    // Driver arrived
    public void markDriverArrived() {
        transitionTo(RideStatus.DRIVER_ARRIVED);
    }
    // Ride started
    public void rideStarted() {
        transitionTo(RideStatus.IN_PROGRESS);
        this.startedAt = LocalDateTime.now();
    }
    // Ride completed
    public void rideCompleted() {
        transitionTo(RideStatus.COMPLETED);
        this.completedAt = LocalDateTime.now();
    }
    // Ride canceled
    public void rideCancelled() {
        transitionTo(RideStatus.CANCELLED);
        this.cancelledAt = LocalDateTime.now();
    }

    public void transitionTo(RideStatus nxtStatus) {
        if (!this.status.moveTo(nxtStatus))
            throw new IllegalStatusChangeException(this.status, nxtStatus);
        this.status = nxtStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRiderId() {
        return riderId;
    }

    public void setRiderId(UUID riderId) {
        this.riderId = riderId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public Double getDropoffLatitude() {
        return dropoffLatitude;
    }

    public Double getDropoffLongitude() {
        return dropoffLongitude;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getMatchedAt() {
        return matchedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}