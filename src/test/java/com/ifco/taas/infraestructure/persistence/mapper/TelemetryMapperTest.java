package com.ifco.taas.infraestructure.persistence.mapper;

import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.persistence.entity.TelemetryEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TelemetryMapperTest {

    private final TelemetryMapper mapper = new TelemetryMapper();

    @Test
    void shouldMapFromDomainToEntity() {
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");

        Telemetry domain = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();

        TelemetryEntity entity = mapper.toEntity(domain);

        assertThat(deviceId).isEqualTo(entity.getDeviceId());
        assertThat(measurement).isEqualTo(entity.getMeasurement());
        assertThat(date).isEqualTo(entity.getDate());
    }

    @Test
    void shouldMapFromEntityToDomain() {
        Long id = 1L;
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");
        LocalDateTime createdAt = LocalDateTime.now();

        TelemetryEntity entity = TelemetryEntity.builder()
                .id(id)
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .createdAt(createdAt)
                .build();

        Telemetry domain = mapper.toDomain(entity);

        assertThat(id).isEqualTo(domain.getId());
        assertThat(deviceId).isEqualTo(domain.getDeviceId());
        assertThat(measurement).isEqualTo(domain.getMeasurement());
        assertThat(date).isEqualTo(domain.getDate());
        assertThat(createdAt).isEqualTo(domain.getCreatedAt());
    }
}
