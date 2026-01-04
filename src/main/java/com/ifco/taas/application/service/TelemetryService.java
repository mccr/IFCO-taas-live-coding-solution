package com.ifco.taas.application.service;

import com.ifco.taas.application.MessagePublisher;
import com.ifco.taas.application.repository.TelemetryRepository;
import com.ifco.taas.application.usecase.RecordTelemetryUseCase;
import com.ifco.taas.domain.Telemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TelemetryService implements RecordTelemetryUseCase {
    private final TelemetryRepository repository;
    private final MessagePublisher messagePublisher;

    @Override
    public Telemetry record(String deviceId, Double measurement, Instant date) {
        Telemetry telemetry = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();

        Telemetry saved = repository.save(telemetry);
        messagePublisher.publish(saved);

        return saved;
    }
}
