package com.ifco.taas.kafka;

import com.ifco.taas.device.DeviceStatusService;
import com.ifco.taas.telemetry.Telemetry;
import com.ifco.taas.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelemetryConsumer {
    private final DeviceStatusService deviceStatusService;

    public TelemetryConsumer(DeviceStatusService deviceStatusService) {
        this.deviceStatusService = deviceStatusService;
    }

    @KafkaListener(
            topics = KafkaConfig.TELEMETRY_TOPIC,
            groupId = "telemetry-consumer-group"
    )
    public void consumeTelemetry(Telemetry telemetry) {
        log.info("Received telemetry: deviceId={}, measurement={}, date={}",
                telemetry.getDeviceId(),
                telemetry.getMeasurement(),
                telemetry.getDate());

        deviceStatusService.updateLatestStatus(telemetry);
    }
}
