package com.ifco.taas.telemetry;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    @PostMapping
    public ResponseEntity<Void> recordTelemetry() {

        // INSERT_YOUR_CODE

        return ResponseEntity.accepted().build();
    }
}
