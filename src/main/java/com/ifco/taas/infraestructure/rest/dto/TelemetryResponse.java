package com.ifco.taas.infraestructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryResponse {
    private Long id;
    private String deviceId;
    private Double measurement;
    private Instant date;
    private LocalDateTime createdAt;
}
