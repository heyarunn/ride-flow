package com.rideflow.ride_service.messaging;

import com.rideflow.common_events.event.RideRequestedEvent;
import com.rideflow.common_events.event.RideflowEvent;
import com.rideflow.common_events.topic.Topics;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "rideflow.kafka.test", havingValue = "true")
public class KafkaTest {
    private final KafkaTemplate<String, RideflowEvent> kafkaTemplate;

    public KafkaTest(KafkaTemplate<String, RideflowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // producer: send events when the application is up
    @EventListener(ApplicationReadyEvent.class)
    public void publishRideEvent() {
        RideRequestedEvent rideRequestedEvent = RideRequestedEvent.of(
                UUID.randomUUID(), UUID.randomUUID(), 10.96, 45.58, 20.23, 50.58
        );

        // sending kafka event
        kafkaTemplate.send(rideRequestedEvent.topic(), rideRequestedEvent.partitionKey(), rideRequestedEvent)
                .whenComplete((result, ex) -> {
                    if (ex!=null) {
                        // throw new Exception(ex.getMessage());
                        System.out.println(ex.getMessage());
                    }
                });
    }

    @KafkaListener(topics = Topics.RIDE_EVENTS, groupId = "ride-service-kafka-test")
    public void onRideEvent(ConsumerRecord<String, RideflowEvent> record) {
        // implementation
    }
}
