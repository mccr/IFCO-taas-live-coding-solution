package com.ifco.taas.infraestructure.persistence.mapper;

import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.infraestructure.persistence.entity.DeviceStatusEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeviceStatusMapperTest {

    private final DeviceStatusMapper mapper = new DeviceStatusMapper();

    @Test
    void shouldMapFromDomainToEntity() {
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");

        DeviceStatus domain = DeviceStatus.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .build();

        DeviceStatusEntity entity = mapper.toEntity(domain);

        assertThat(deviceId).isEqualTo(entity.getDeviceId());
        assertThat(measurement).isEqualTo(entity.getLatestMeasurement());
        assertThat(date).isEqualTo(entity.getLatestDate());
    }

    @Test
    void shouldMapFromEntityToDomain() {
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");
        LocalDateTime updatedAt = LocalDateTime.now();

        DeviceStatusEntity entity = DeviceStatusEntity.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .updatedAt(updatedAt)
                .build();

        DeviceStatus domain = mapper.toDomain(entity);

        assertThat(deviceId).isEqualTo(domain.getDeviceId());
        assertThat(measurement).isEqualTo(domain.getLatestMeasurement());
        assertThat(date).isEqualTo(domain.getLatestDate());
        assertThat(updatedAt).isEqualTo(domain.getUpdatedAt());
    }
}
