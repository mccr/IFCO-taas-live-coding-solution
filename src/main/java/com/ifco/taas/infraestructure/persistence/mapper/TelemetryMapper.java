package com.ifco.taas.infraestructure.persistence.mapper;

import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.persistence.entity.TelemetryEntity;
import org.springframework.stereotype.Component;

@Component
public class TelemetryMapper {
    public TelemetryEntity toEntity(Telemetry domain) {
        return TelemetryEntity.builder()
                .deviceId(domain.getDeviceId())
                .measurement(domain.getMeasurement())
                .date(domain.getDate())
                .build();
    }

    public Telemetry toDomain(TelemetryEntity entity) {
        return Telemetry.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .measurement(entity.getMeasurement())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
