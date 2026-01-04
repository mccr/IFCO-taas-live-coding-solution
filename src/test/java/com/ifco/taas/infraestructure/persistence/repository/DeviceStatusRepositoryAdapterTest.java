package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.infraestructure.persistence.entity.DeviceStatusEntity;
import com.ifco.taas.infraestructure.persistence.mapper.DeviceStatusMapper;
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
public class DeviceStatusRepositoryAdapterTest {

    @Mock
    private DeviceStatusJpaRepository jpaRepository;

    @Mock
    private DeviceStatusMapper mapper;

    @InjectMocks
    private DeviceStatusRepositoryAdapter adapter;

    @Test
    void shouldConvertAndSaveDeviceStatus() {
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");
        LocalDateTime updatedAt = LocalDateTime.now();

        DeviceStatus domain = DeviceStatus.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .build();

        DeviceStatusEntity entity = DeviceStatusEntity.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .build();

        DeviceStatus savedDomain = DeviceStatus.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .updatedAt(updatedAt)
                .build();

        DeviceStatusEntity savedEntity = DeviceStatusEntity.builder()
                .deviceId(deviceId)
                .latestMeasurement(measurement)
                .latestDate(date)
                .updatedAt(updatedAt)
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        DeviceStatus result = adapter.save(domain);

        assertThat(result).isNotNull();
        verify(mapper).toEntity(domain);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }
}
