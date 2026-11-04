package com.rideflow.common_events.config;

import com.rideflow.common_events.topic.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@AutoConfiguration
@ConditionalOnClass(KafkaAdmin.class)
public class RideflowTopicsAutoConfiguration {

    public NewTopic rideEventsTopic() {
        return TopicBuilder.name(Topics.RIDE_EVENTS).partitions(Topics.PARTITIONS).replicas(1).build();
    }

    public NewTopic driverEventsTopic() {
        return TopicBuilder.name(Topics.DRIVER_EVENTS).partitions(Topics.PARTITIONS).replicas(1).build();
    }

    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name(Topics.PAYMENT_EVENTS).partitions(Topics.PARTITIONS).replicas(1).build();
    }

}
