package com.rideflow.common_events.topic;

// one topic per domain
public class Topics {
    public static final String RIDE_EVENTS = "ride-events";
    public static final String DRIVER_EVENTS = "driver-events";
    public static final String PAYMENT_EVENTS = "payment-events";

    public static final int PARTITIONS = 3;

    private Topics() {
    }
}
