package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID paymentId,
        UUID rideId,
        UUID riderId,
        String reason
) implements RideflowEvent {
    public static PaymentFailedEvent of(UUID rideId, UUID riderId,
                                        UUID paymentId, String reason) {
        return new PaymentFailedEvent(UUID.randomUUID(), LocalDateTime.now(), paymentId,rideId, riderId, reason);
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
