package com.ifco.taas.kafka;

import com.ifco.taas.telemetry.Telemetry;
import com.ifco.taas.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelemetryProducer {
    private final KafkaTemplate<String, Telemetry> kafkaTemplate;

    public TelemetryProducer(KafkaTemplate<String, Telemetry> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTelemetry(Telemetry telemetry) {
        log.info("Sending telemetry: deviceId={}", telemetry.getDeviceId());

        kafkaTemplate.send(KafkaConfig.TELEMETRY_TOPIC, telemetry.getDeviceId(), telemetry)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully: {}", result.getRecordMetadata());
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }
}
