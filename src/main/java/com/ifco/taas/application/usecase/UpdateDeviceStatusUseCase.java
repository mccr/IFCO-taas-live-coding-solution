package com.ifco.taas.application.usecase;

import com.ifco.taas.domain.Telemetry;

public interface UpdateDeviceStatusUseCase {
    void updateFrom(Telemetry telemetry);
}
