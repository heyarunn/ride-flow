package com.rideflow.ride_service.domain;

import java.util.HashSet;
import java.util.Set;

public enum RideStatus {
    REQUESTED, MATCHED, DRIVER_ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED;

    private Set<RideStatus> nextStatus;

    static {
        REQUESTED.nextStatus = new HashSet<>(Set.of(MATCHED, CANCELLED));
        MATCHED.nextStatus = new HashSet<>(Set.of(DRIVER_ARRIVED, CANCELLED));
        DRIVER_ARRIVED.nextStatus = new HashSet<>(Set.of(IN_PROGRESS, CANCELLED));
        IN_PROGRESS.nextStatus = new HashSet<>(Set.of(COMPLETED, CANCELLED));
        COMPLETED.nextStatus = new HashSet<>(Set.of()); // terminal
        CANCELLED.nextStatus = new HashSet<>(Set.of()); // terminal
    }

    public boolean moveTo(RideStatus moveToStatus) {
        return nextStatus.contains(moveToStatus);
    }

    public boolean isTerminal() {
        return nextStatus.isEmpty();
    }
}
