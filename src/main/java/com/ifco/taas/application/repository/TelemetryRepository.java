package com.ifco.taas.application.repository;

import com.ifco.taas.domain.Telemetry;

public interface TelemetryRepository {
    Telemetry save(Telemetry telemetry);
}
