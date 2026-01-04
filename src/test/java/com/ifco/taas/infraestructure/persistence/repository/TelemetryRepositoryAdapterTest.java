package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.persistence.entity.TelemetryEntity;
import com.ifco.taas.infraestructure.persistence.mapper.TelemetryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelemetryRepositoryAdapterTest {

    @Mock
    private TelemetryJpaRepository jpaRepository;

    @Mock
    private TelemetryMapper mapper;

    @InjectMocks
    private TelemetryRepositoryAdapter adapter;

    @Test
    void shouldConvertAndSaveTelemetry() {
        Long id = 1L;
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");
        LocalDateTime createdAt = LocalDateTime.now();

        Telemetry domain = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();

        TelemetryEntity entity = TelemetryEntity.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();

        TelemetryEntity savedEntity = TelemetryEntity.builder()
                .id(id)
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .createdAt(createdAt)
                .build();

        Telemetry savedDomain = Telemetry.builder()
                .id(id)
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .createdAt(createdAt)
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        Telemetry result = adapter.save(domain);

        assertThat(result).isNotNull();
        verify(mapper).toEntity(domain);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }
}
