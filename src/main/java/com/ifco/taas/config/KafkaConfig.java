package com.ifco.taas.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String TELEMETRY_TOPIC = "telemetry-events";

    @Bean
    public NewTopic telemetryTopic() {
        return TopicBuilder.name(TELEMETRY_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
