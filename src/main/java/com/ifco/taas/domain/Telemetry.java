package com.ifco.taas.domain;

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
public class Telemetry {
    private Long id;
    private String deviceId;
    private Double measurement;
    private Instant date;
    private LocalDateTime createdAt;
}
