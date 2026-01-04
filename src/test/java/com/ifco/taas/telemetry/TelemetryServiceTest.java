package com.ifco.taas.telemetry;

import com.ifco.taas.kafka.TelemetryProducer;
import com.ifco.taas.telemetry.dto.TelemetryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
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

        Telemetry savedTelemetry = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(Instant.parse(date))
                .build();

        when(repository.save(any(Telemetry.class))).thenReturn(savedTelemetry);

        Telemetry result = service.recordTelemetry(request);

        assertThat(result).isNotNull();
        assertThat(deviceId).isEqualTo(result.getDeviceId());
        assertThat(measurement).isEqualTo(result.getMeasurement());
        assertThat(date).isEqualTo(result.getDate().toString());

        verify(repository, times(1)).save(any(Telemetry.class));
        verify(producer, times(1)).sendTelemetry(savedTelemetry);
    }
}