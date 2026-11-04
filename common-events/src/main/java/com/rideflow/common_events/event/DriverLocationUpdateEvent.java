package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DriverLocationUpdateEvent(
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID driverId,
        double latitude,
        double longitude
) implements RideflowEvent {
    public static DriverLocationUpdateEvent of(UUID driverId, double latitude, double longitude) {
        return new DriverLocationUpdateEvent(UUID.randomUUID(), LocalDateTime.now(), driverId, latitude, longitude);
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