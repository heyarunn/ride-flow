package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.time.LocalDateTime;
import java.util.UUID;

public record RideMatchedEvent(
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID rideId,
        UUID riderId,
        UUID driverId
    ) implements RideflowEvent{
    public static RideMatchedEvent of(UUID rideId, UUID riderId, UUID driverId){
        return new RideMatchedEvent(UUID.randomUUID(), LocalDateTime.now(), rideId, riderId, driverId);
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
