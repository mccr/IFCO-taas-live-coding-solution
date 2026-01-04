package com.ifco.taas.application;

import com.ifco.taas.domain.Telemetry;

public interface MessagePublisher {
    void publish(Telemetry telemetry);
}
