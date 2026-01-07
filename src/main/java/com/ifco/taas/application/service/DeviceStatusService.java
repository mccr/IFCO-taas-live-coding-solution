package com.ifco.taas.application.service;

import com.ifco.taas.application.repository.DeviceStatusRepository;
import com.ifco.taas.application.usecase.GetDeviceStatusUseCase;
import com.ifco.taas.application.usecase.UpdateDeviceStatusUseCase;
import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.domain.Telemetry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceStatusService implements UpdateDeviceStatusUseCase, GetDeviceStatusUseCase {

    private final DeviceStatusRepository repository;

    @Override
    @Transactional
    public void updateFrom(Telemetry telemetry) {
        repository.findById(telemetry.getDeviceId())
                .ifPresentOrElse(
                        existingStatus -> updateExistingStatus(existingStatus, telemetry),
                        () -> createDeviceStatus(telemetry)
                );
    }

    public List<DeviceStatus> getAllDeviceStatuses() {
        return repository.findAll();
    }

    private void updateExistingStatus(DeviceStatus existingStatus, Telemetry telemetry) {
        String deviceId = telemetry.getDeviceId();
        Instant receivedDate = telemetry.getDate();
        Instant latestDate = existingStatus.getLatestDate();

        int comparison = receivedDate.compareTo(latestDate);

        if (comparison > 0) {
            updateDeviceStatus(existingStatus, telemetry);
        } else if (comparison == 0) {
            log.warn("Duplicate telemetry detected for device {} at {}",
                    deviceId, telemetry.getDate());
        } else {
            log.warn("Old telemetry data for device {}. Received: {}, Latest: {}",
                    deviceId, telemetry.getDate(), existingStatus.getLatestDate());
        }
    }

    private void updateDeviceStatus(DeviceStatus existingStatus, Telemetry telemetry) {
        log.info("Updating device status for device: {}", telemetry.getDeviceId());

        DeviceStatus updated = existingStatus.toBuilder()
                .latestMeasurement(telemetry.getMeasurement())
                .latestDate(telemetry.getDate())
                .build();

        repository.save(updated);
    }

    private void createDeviceStatus(Telemetry telemetry) {
        log.info("Creating new device status for device: {}", telemetry.getDeviceId());

        DeviceStatus newStatus = DeviceStatus.builder()
                .deviceId(telemetry.getDeviceId())
                .latestMeasurement(telemetry.getMeasurement())
                .latestDate(telemetry.getDate())
                .build();

        repository.save(newStatus);
    }

}
