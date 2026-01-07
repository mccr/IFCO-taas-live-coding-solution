package com.ifco.taas.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDateTime;

@Value
@Builder
public class Telemetry {
    Long id;
    String deviceId;
    Double measurement;
    Instant date;
    LocalDateTime createdAt;
}
