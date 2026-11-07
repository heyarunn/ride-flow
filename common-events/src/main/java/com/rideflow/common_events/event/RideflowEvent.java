package com.rideflow.common_events.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RideStartedEvent.class, name = "ride.started"),
})
public sealed interface RideflowEvent
        permits DriverLocationUpdateEvent,
        DriverStatusChangedEvent,
        PaymentCompletedEvent,
        PaymentFailedEvent,
        RideCancelledEvent,
        RideCompletedEvent,
        RideMatchedEvent,
        RideRequestedEvent,
        RideStartedEvent {
    UUID eventId();
    LocalDateTime occuredAt();
    String topic();
    String partitionKey();
}
