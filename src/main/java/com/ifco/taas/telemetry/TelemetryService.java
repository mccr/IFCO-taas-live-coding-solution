package com.ifco.taas.telemetry;

import com.ifco.taas.kafka.TelemetryProducer;
import com.ifco.taas.telemetry.dto.TelemetryRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TelemetryService {
    private final TelemetryRepository repository;
    private final TelemetryProducer producer;

    public TelemetryService(TelemetryRepository repository, TelemetryProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Telemetry recordTelemetry(TelemetryRequest request) {
        Telemetry telemetry = Telemetry.builder()
                .deviceId(request.getDeviceId())
                .measurement(request.getMeasurement())
                .date(Instant.parse(request.getDate()))
                .build();

        Telemetry saved = repository.save(telemetry);

        producer.sendTelemetry(saved);

        return saved;
    }
}
