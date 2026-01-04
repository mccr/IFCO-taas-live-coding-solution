package com.ifco.taas.application.usecase;

import com.ifco.taas.domain.Telemetry;

import java.time.Instant;

public interface RecordTelemetryUseCase {
    Telemetry record(String deviceId, Double measurement, Instant date);
}
