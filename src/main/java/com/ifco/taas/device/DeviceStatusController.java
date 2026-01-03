package com.ifco.taas.device;

import com.ifco.taas.device.dto.DeviceStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device-status")
@Slf4j
public class DeviceStatusController {

    private final DeviceStatusService service;

    @Autowired
    public DeviceStatusController(DeviceStatusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllDeviceStatuses() {
        try {
            List<DeviceStatusResponse> statuses = service.getAllDeviceStatuses();
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            log.error("Error getting devices statuses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading latest statuses - " + e.getMessage());
        }
    }
}
