package com.ifco.taas.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class DeviceStatus {
    String deviceId;
    Double latestMeasurement;
    Instant latestDate;
    LocalDateTime updatedAt;
}
