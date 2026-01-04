package com.ifco.taas.device;

import com.ifco.taas.device.dto.DeviceStatusResponse;
import com.ifco.taas.telemetry.Telemetry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceStatusService {

    private final DeviceStatusRepository repository;

    public DeviceStatusService(DeviceStatusRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void updateLatestStatus(Telemetry telemetry) {
        repository.findById(telemetry.getDeviceId())
                .ifPresentOrElse(
                        existingStatus -> updateExistingStatus(existingStatus, telemetry),
                        () -> createDeviceStatus(telemetry)
                );
    }

    public List<DeviceStatusResponse> getAllDeviceStatuses() {
        return repository.findAll()
                .stream()
                .map(DeviceStatusResponse::from)
                .collect(Collectors.toList());
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

    private void updateDeviceStatus(DeviceStatus status, Telemetry telemetry) {
        log.info("Updating device status for device: {}", telemetry.getDeviceId());

        status.setLatestMeasurement(telemetry.getMeasurement());
        status.setLatestDate(telemetry.getDate());

        repository.save(status);
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
