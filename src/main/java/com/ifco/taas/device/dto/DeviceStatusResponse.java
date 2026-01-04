package com.ifco.taas.device.dto;

import com.ifco.taas.device.DeviceStatus;
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

    public static DeviceStatusResponse from(DeviceStatus entity) {
        return DeviceStatusResponse.builder()
                .deviceId(entity.getDeviceId())
                .latestMeasurement(entity.getLatestMeasurement())
                .latestDate(entity.getLatestDate())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
