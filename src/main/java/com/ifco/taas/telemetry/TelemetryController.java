package com.ifco.taas.telemetry;

import com.ifco.taas.telemetry.dto.TelemetryRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telemetry")
@Slf4j
public class TelemetryController {
    private final TelemetryService service;

    @Autowired
    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> recordTelemetry(@Valid @RequestBody TelemetryRequest request) {
        try {
            Telemetry recorded = service.recordTelemetry(request);
            return ResponseEntity.ok(recorded);
        } catch (Exception e) {
            log.error("Error recording telemetry for device: {}", request.getDeviceId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error recording telemetry - " + e.getMessage());
        }
    }
}
