package com.ifco.taas.infraestructure.persistence.mapper;

import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.infraestructure.persistence.entity.DeviceStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class DeviceStatusMapper {

    public DeviceStatusEntity toEntity(DeviceStatus domain) {
        return DeviceStatusEntity.builder()
                .deviceId(domain.getDeviceId())
                .latestMeasurement(domain.getLatestMeasurement())
                .latestDate(domain.getLatestDate())
                .build();
    }

    public DeviceStatus toDomain(DeviceStatusEntity entity) {
        return DeviceStatus.builder()
                .deviceId(entity.getDeviceId())
                .latestMeasurement(entity.getLatestMeasurement())
                .latestDate(entity.getLatestDate())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
