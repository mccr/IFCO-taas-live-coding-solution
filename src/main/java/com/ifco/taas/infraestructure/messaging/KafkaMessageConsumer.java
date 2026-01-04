package com.ifco.taas.infraestructure.messaging;

import com.ifco.taas.application.usecase.UpdateDeviceStatusUseCase;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageConsumer {
    private final UpdateDeviceStatusUseCase useCase;

    @KafkaListener(
            topics = KafkaConfig.TELEMETRY_TOPIC,
            groupId = "telemetry-consumer-group"
    )
    public void consumeTelemetry(Telemetry telemetry) {
        log.info("Received telemetry: deviceId={}, measurement={}, date={}",
                telemetry.getDeviceId(),
                telemetry.getMeasurement(),
                telemetry.getDate());

        useCase.updateFrom(telemetry);
    }
}
