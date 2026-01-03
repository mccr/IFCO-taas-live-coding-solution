package com.ifco.taas.device;

import com.ifco.taas.device.dto.DeviceStatusResponse;
import com.ifco.taas.telemetry.Telemetry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        String deviceId = telemetry.getDeviceId();
        Optional<DeviceStatus> status = repository.findById(deviceId);

        if (status.isPresent()) {
            DeviceStatus existingStatus = status.get();
            if (telemetry.getDate().isAfter(existingStatus.getLatestDate())) {
                log.info("Updating device status for {}", telemetry.getDeviceId());

                existingStatus.setLatestMeasurement(telemetry.getMeasurement());
                existingStatus.setLatestDate(telemetry.getDate());

                repository.save(existingStatus);
            } else if (telemetry.getDate().equals(existingStatus.getLatestDate())) {
                log.warn("Duplicate telemetry detected for device {} at {}",
                        deviceId, telemetry.getDate());
            } else {
                log.warn("Old telemetry data for device {}. Received: {}, Latest: {}",
                        deviceId, telemetry.getDate(), existingStatus.getLatestDate());
            }
        } else {
            log.info("Creating new device status for {}", deviceId);

            DeviceStatus newDeviceStatus = new DeviceStatus();
            newDeviceStatus.setDeviceId(telemetry.getDeviceId());
            newDeviceStatus.setLatestMeasurement(telemetry.getMeasurement());
            newDeviceStatus.setLatestDate(telemetry.getDate());

            repository.save(newDeviceStatus);
        }
    }

    public List<DeviceStatusResponse> getAllDeviceStatuses() {
        return repository.findAll()
                .stream()
                .map(DeviceStatusResponse::from)
                .collect(Collectors.toList());
    }
}
