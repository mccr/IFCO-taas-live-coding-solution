package com.ifco.taas.infraestructure.messaging;

import com.ifco.taas.application.messaging.MessagePublisher;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagePublisher implements MessagePublisher {
    private final KafkaTemplate<String, Telemetry> kafkaTemplate;

    @Override
    public void publish(Telemetry telemetry) {
        log.info("Sending telemetry: deviceId={}", telemetry.getDeviceId());

        kafkaTemplate.send(KafkaConfig.TELEMETRY_TOPIC, telemetry.getDeviceId(), telemetry)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully: {}", result);
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }
}
