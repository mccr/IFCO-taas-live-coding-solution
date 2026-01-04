package com.ifco.taas.application.messaging;

import com.ifco.taas.domain.Telemetry;

public interface MessagePublisher {
    void publish(Telemetry telemetry);
}
