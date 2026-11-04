package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rideflow.common_events.topic.Topics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID eventId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime occuredAt,
        UUID paymentId,
        UUID rideId,
        UUID riderId,
        BigDecimal amount,
        String currency
) implements RideflowEvent {
    public static PaymentCompletedEvent of(UUID rideId, UUID riderId,
                                           UUID paymentId, BigDecimal amount, String currency) {
        return new PaymentCompletedEvent(UUID.randomUUID(), LocalDateTime.now(), paymentId,rideId, riderId, amount, currency);
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