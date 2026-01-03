package com.ifco.taas.device;

import com.ifco.taas.telemetry.Telemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceStatusServiceTest {

    @Mock
    private DeviceStatusRepository repository;

    @InjectMocks
    private DeviceStatusService service;

    private String deviceID;
    private Double latestMeasurement;
    private Instant latestDate;
    private Telemetry telemetry;

    @BeforeEach
    void setUp() {
        deviceID = "1";
        latestMeasurement = 10.5;
        latestDate = Instant.parse("2025-01-31T13:00:00Z");

        telemetry = new Telemetry();
        telemetry.setDeviceId(deviceID);
        telemetry.setMeasurement(latestMeasurement);
        telemetry.setDate(latestDate);
    }

    @Test
    void shouldUpdateLatestStatusIfExisting() {
        DeviceStatus existingDeviceStatus = new DeviceStatus();
        existingDeviceStatus.setDeviceId(deviceID);
        existingDeviceStatus.setLatestMeasurement(20.0);
        existingDeviceStatus.setLatestDate(Instant.parse("2025-01-30T12:00:00Z"));

        when(repository.findById(deviceID)).thenReturn(Optional.of(existingDeviceStatus));

        service.updateLatestStatus(telemetry);

        existingDeviceStatus.setLatestMeasurement(latestMeasurement);
        existingDeviceStatus.setLatestDate(latestDate);

        verify(repository, times(1)).save(existingDeviceStatus);
    }

    @Test
    void shouldSkipUpdateLatestStatusIfExistingDataIsMoreRecent() {
        DeviceStatus existingDeviceStatus = new DeviceStatus();
        existingDeviceStatus.setDeviceId(deviceID);
        existingDeviceStatus.setLatestMeasurement(20.0);
        existingDeviceStatus.setLatestDate(Instant.parse("2025-01-31T15:00:00Z"));

        when(repository.findById(deviceID)).thenReturn(Optional.of(existingDeviceStatus));

        service.updateLatestStatus(telemetry);

        existingDeviceStatus.setLatestMeasurement(latestMeasurement);
        existingDeviceStatus.setLatestDate(latestDate);

        verify(repository, never()).save(existingDeviceStatus);
    }

    @Test
    void shouldSkipUpdateLatestStatusIfExistingDataIsExactlyTheSame() {
        DeviceStatus existingDeviceStatus = new DeviceStatus();
        existingDeviceStatus.setDeviceId(deviceID);
        existingDeviceStatus.setLatestMeasurement(latestMeasurement);
        existingDeviceStatus.setLatestDate(latestDate);

        when(repository.findById(deviceID)).thenReturn(Optional.of(existingDeviceStatus));

        service.updateLatestStatus(telemetry);

        verify(repository, never()).save(existingDeviceStatus);
    }

    @Test
    void shouldCreateLatestStatusIfNoExistingData() {
        when(repository.findById(deviceID)).thenReturn(Optional.empty());

        service.updateLatestStatus(telemetry);

        DeviceStatus deviceStatusSaved = new DeviceStatus();
        deviceStatusSaved.setDeviceId(deviceID);
        deviceStatusSaved.setLatestMeasurement(latestMeasurement);
        deviceStatusSaved.setLatestDate(latestDate);

        verify(repository, times(1)).save(deviceStatusSaved);
    }
}
