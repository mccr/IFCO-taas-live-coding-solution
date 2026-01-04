package com.ifco.taas.infraestructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusResponse {
    private String deviceId;
    private Double latestMeasurement;
    private Instant latestDate;
    private LocalDateTime updatedAt;
}
