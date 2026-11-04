package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.time.LocalDateTime;
import java.util.UUID;

public record DriverStatusChangedEvent(
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID driverId,
        String previousStatus,
        String newStatus
) implements RideflowEvent {
    public static DriverStatusChangedEvent of(UUID driverId, String previousState, String newStatus) {
        return new DriverStatusChangedEvent(UUID.randomUUID(), LocalDateTime.now(), driverId, previousState, newStatus);
    }

    @Override
    public String topic() {
        return Topics.RIDE_EVENTS;
    }

    @Override
    public String partitionKey() {
        return driverId.toString();
    }
}