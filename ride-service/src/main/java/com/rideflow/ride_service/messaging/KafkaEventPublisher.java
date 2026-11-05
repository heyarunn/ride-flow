package com.rideflow.ride_service.messaging;

import com.rideflow.common_events.event.RideflowEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, RideflowEvent> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, RideflowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void publishEvent(RideflowEvent event) {
        kafkaTemplate.send(event.topic(), event.partitionKey(), event)
                .whenComplete((result, ex) -> {
                    if (ex!=null) {
                        System.out.println(ex.getMessage());
                    }
                });
    }
}
