package com.rideflow.payment_service.service;

import com.rideflow.common_events.event.PaymentCompletedEvent;
import com.rideflow.common_events.event.PaymentFailedEvent;
import com.rideflow.common_events.event.RideCompletedEvent;
import com.rideflow.common_events.event.RideflowEvent;
import com.rideflow.common_events.topic.Topics;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PaymentProcessor {

    private static final String CARD_DECLINED = "card declined error";

    private final KafkaTemplate<String, RideflowEvent> kafkaTemplate;
    private final double failureRate;
    private final String currency;

    public PaymentProcessor(KafkaTemplate<String, RideflowEvent> kafkaTemplate, double failureRate, String currency) {
        this.kafkaTemplate = kafkaTemplate;
        this.failureRate = failureRate;
        this.currency = currency;
    }

    @KafkaListener(topics = Topics.RIDE_EVENTS)
    public void onRideEvent(ConsumerRecord<String, RideflowEvent> record) {
        if (record.value() instanceof RideCompletedEvent rideCompletedEvent) {
            System.out.println("Completed event");
        }
        else {
            System.out.println("not completed");
        }
    }

    private void chargeRide(RideCompletedEvent rideEvent) {
        UUID paymentId = UUID.randomUUID();
        RideflowEvent rideflowEvent;

        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            rideflowEvent = PaymentFailedEvent.of(rideEvent.rideId(), rideEvent.riderId(), paymentId, CARD_DECLINED);
        }
        else {
            BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(80.0,500.0))
                    .setScale(2, RoundingMode.HALF_UP);
            rideflowEvent = PaymentCompletedEvent.of(rideEvent.rideId(), rideEvent.riderId(), paymentId, amount, currency);
        }

        kafkaTemplate.send(rideflowEvent.topic(), rideflowEvent.partitionKey(), rideflowEvent)
                .whenComplete((result, ex) -> {
                    if (ex!=null) {
                        System.out.println(ex.getMessage());
                    }
                });

    }
}