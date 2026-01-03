package com.ifco.taas.telemetry;

import com.ifco.taas.kafka.TelemetryProducer;
import com.ifco.taas.telemetry.dto.TelemetryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TelemetryServiceTest {

    @Mock
    private TelemetryRepository repository;

    @Mock
    private TelemetryProducer producer;

    @InjectMocks
    private TelemetryService service;

    @Test
    void shouldRecordTelemetryAndSendMessage() {
        String deviceId = "1";
        Double measurement = 25.5;
        String date = "2025-01-31T13:00:00Z";

        TelemetryRequest request = new TelemetryRequest();
        request.setDeviceId(deviceId);
        request.setMeasurement(measurement);
        request.setDate(date);

        Telemetry savedTelemetry = new Telemetry();
        savedTelemetry.setDeviceId(deviceId);
        savedTelemetry.setMeasurement(measurement);
        savedTelemetry.setDate(Instant.parse(date));

        when(repository.save(any(Telemetry.class))).thenReturn(savedTelemetry);

        Telemetry result = service.recordTelemetry(request);

        assertNotNull(result);
        assertEquals(deviceId, result.getDeviceId());
        assertEquals(measurement, result.getMeasurement());
        assertEquals(date, result.getDate().toString());

        verify(repository, times(1)).save(any(Telemetry.class));
        verify(producer, times(1)).sendTelemetry(savedTelemetry);
    }
}