package com.ifco.taas.application.service;

import com.ifco.taas.application.repository.TelemetryRepository;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.messaging.KafkaMessagePublisher;
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
    private KafkaMessagePublisher messagePublisher;

    @InjectMocks
    private TelemetryService service;

    @Test
    void shouldRecordTelemetryAndSendMessage() {
        String deviceId = "1";
        Double measurement = 25.5;
        Instant date = Instant.parse("2025-01-31T13:00:00Z");

        Telemetry savedTelemetry = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();

        when(repository.save(any(Telemetry.class))).thenReturn(savedTelemetry);

        Telemetry result = service.record(deviceId, measurement, date);

        assertThat(result).isNotNull();
        assertThat(deviceId).isEqualTo(result.getDeviceId());
        assertThat(measurement).isEqualTo(result.getMeasurement());
        assertThat(date).isEqualTo(result.getDate().toString());

        verify(repository, times(1)).save(any(Telemetry.class));
        verify(messagePublisher, times(1)).publish(savedTelemetry);
    }
}