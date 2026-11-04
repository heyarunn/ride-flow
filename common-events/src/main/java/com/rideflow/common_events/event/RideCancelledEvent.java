package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.time.LocalDateTime;
import java.util.UUID;

public record RideCancelledEvent (
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID rideId,
        UUID riderId,
        UUID driverId,
        String reason
) implements RideflowEvent {
    public static RideCancelledEvent of(UUID rideId, UUID riderId, UUID driverId, String reason) {
        return new RideCancelledEvent(UUID.randomUUID(), LocalDateTime.now(),rideId, riderId, driverId, reason);
    }

    @Override
    public String topic() {
        return Topics.RIDE_EVENTS;
    }

    @Override
    public String partitionKey() {
        return rideId.toString();
    }
}