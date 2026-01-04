package com.ifco.taas.infraestructure.rest;

import com.ifco.taas.application.usecase.RecordTelemetryUseCase;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.rest.dto.TelemetryRequest;
import com.ifco.taas.infraestructure.rest.dto.TelemetryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/telemetry")
@RequiredArgsConstructor
@Slf4j
public class TelemetryController {
    private final RecordTelemetryUseCase useCase;

    @PostMapping
    public ResponseEntity<?> recordTelemetry(@Valid @RequestBody TelemetryRequest request) {
        try {
            Telemetry recorded = useCase.record(
                    request.getDeviceId(),
                    request.getMeasurement(),
                    Instant.parse(request.getDate()));

            return ResponseEntity.ok(toResponse(recorded));
        } catch (Exception e) {
            log.error("Error recording telemetry for device: {}", request.getDeviceId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error recording telemetry - " + e.getMessage());
        }
    }

    private TelemetryResponse toResponse(Telemetry telemetry) {
        return TelemetryResponse.builder()
                .id(telemetry.getId())
                .deviceId(telemetry.getDeviceId())
                .measurement(telemetry.getMeasurement())
                .date(telemetry.getDate())
                .createdAt(telemetry.getCreatedAt())
                .build();
    }
}
