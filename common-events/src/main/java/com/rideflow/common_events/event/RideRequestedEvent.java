package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.time.LocalDateTime;
import java.util.UUID;

public record RideRequestedEvent(
         UUID eventId,
         @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
         UUID rideId,
         UUID riderId,
         double pickupLatitude,
         double pickupLongitude,
         double dropoffLatitude,
         double dropoffLongitude
) implements RideflowEvent {
    public static RideRequestedEvent of(UUID rideId, UUID riderId,
                                      double pickupLatitude, double pickupLongitude,
                                      double dropoffLatitude, double dropoffLongitude) {
        return new RideRequestedEvent(UUID.randomUUID(), LocalDateTime.now(),rideId, riderId,
                pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude);
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