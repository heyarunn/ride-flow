package com.rideflow.driver_service.messaging;

import com.rideflow.common_events.event.*;
import com.rideflow.common_events.topic.Topics;
import com.rideflow.driver_service.service.DriverService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RideEventListner {
    private final DriverService driverService;

    public RideEventListner(DriverService driverService) {
        this.driverService = driverService;
    }

    @KafkaListener(topics = Topics.RIDE_EVENTS)
    public void onRideEvent(ConsumerRecord<String, RideflowEvent> record) {
        switch (record.value()) {
            case RideMatchedEvent matchedEvent -> driverService.reserveForTrip(matchedEvent.driverId());
            case RideCompletedEvent completedEvent -> driverService.releaseFromTrip(completedEvent.driverId());
            case RideCancelledEvent cancelledEvent -> {
                    if (cancelledEvent.driverId()!=null) {
                        driverService.releaseFromTrip(cancelledEvent.driverId());
                    }
            }
            default -> {

            }
        }
    }
}
