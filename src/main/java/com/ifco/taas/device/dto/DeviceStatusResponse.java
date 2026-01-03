package com.ifco.taas.device.dto;

import com.ifco.taas.device.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DeviceStatusResponse {
    private String deviceId;
    private Double latestMeasurement;
    private Instant latestDate;
    private LocalDateTime updatedAt;

    public static DeviceStatusResponse from(DeviceStatus entity) {
        return new DeviceStatusResponse(
                entity.getDeviceId(),
                entity.getLatestMeasurement(),
                entity.getLatestDate(),
                entity.getUpdatedAt()
        );
    }
}
