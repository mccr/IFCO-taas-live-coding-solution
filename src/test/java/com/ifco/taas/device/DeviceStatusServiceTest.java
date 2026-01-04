package com.ifco.taas.device;

import com.ifco.taas.device.dto.DeviceStatusResponse;
import com.ifco.taas.telemetry.Telemetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    private DeviceStatus existingDeviceStatus;

    @BeforeEach
    void setUp() {
        deviceID = "1";
        latestMeasurement = 10.5;
        latestDate = Instant.parse("2025-01-31T13:00:00Z");

        telemetry = new Telemetry();
        telemetry.setDeviceId(deviceID);
        telemetry.setMeasurement(latestMeasurement);
        telemetry.setDate(latestDate);

        existingDeviceStatus = DeviceStatus.builder()
                .deviceId(deviceID)
                .latestMeasurement(20.5)
                .latestDate(Instant.parse("2025-01-30T13:00:00Z"))
                .build();
    }

    @Test
    void shouldUpdateLatestStatusIfExisting() {
        when(repository.findById(deviceID)).thenReturn(Optional.of(existingDeviceStatus));

        service.updateLatestStatus(telemetry);

        existingDeviceStatus.setLatestMeasurement(latestMeasurement);
        existingDeviceStatus.setLatestDate(latestDate);

        verify(repository, times(1)).save(existingDeviceStatus);
    }

    @Test
    void shouldSkipUpdateLatestStatusIfExistingDataIsMoreRecent() {
        existingDeviceStatus.setLatestDate(Instant.parse("2025-01-31T15:00:00Z"));

        when(repository.findById(deviceID)).thenReturn(Optional.of(existingDeviceStatus));

        service.updateLatestStatus(telemetry);

        verify(repository, never()).save(any(DeviceStatus.class));
    }

    @Test
    void shouldSkipUpdateLatestStatusIfExistingDataIsExactlyTheSame() {
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

        DeviceStatus deviceStatusSaved = DeviceStatus.builder()
                .deviceId(deviceID)
                .latestMeasurement(latestMeasurement)
                .latestDate(latestDate)
                .build();

        verify(repository, times(1)).save(deviceStatusSaved);
    }

    @Test
    void shouldFindAllDeviceStatusesAndMapToDeviceStatusResponse() {
        when(repository.findAll()).thenReturn(List.of(existingDeviceStatus));

        List<DeviceStatusResponse> listResponse = service.getAllDeviceStatuses();

        assertThat(listResponse).isNotNull();

        DeviceStatusResponse response = listResponse.get(0);
        assertThat(deviceID).isEqualTo(response.getDeviceId());
        assertThat(20.5).isEqualTo(response.getLatestMeasurement());
        assertThat(Instant.parse("2025-01-30T13:00:00Z")).isEqualTo(response.getLatestDate());
    }

    @Test
    void shouldReturnEmptyListIfNoDeviceStatusFound() {
        List<DeviceStatusResponse> listResponse = service.getAllDeviceStatuses();

        assertThat(listResponse).isEmpty();
    }
}
