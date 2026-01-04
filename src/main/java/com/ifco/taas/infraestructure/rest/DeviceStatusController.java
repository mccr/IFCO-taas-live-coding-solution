package com.ifco.taas.infraestructure.rest;

import com.ifco.taas.application.usecase.GetDeviceStatusUseCase;
import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.infraestructure.rest.dto.DeviceStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/device-status")
@RequiredArgsConstructor
@Slf4j
public class DeviceStatusController {

    private final GetDeviceStatusUseCase useCase;

    @GetMapping
    public ResponseEntity<?> getAllDeviceStatuses() {
        try {
            List<DeviceStatus> statuses = useCase.getAllDeviceStatuses();
            List<DeviceStatusResponse> response = statuses.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting devices statuses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading latest statuses - " + e.getMessage());
        }
    }

    private DeviceStatusResponse toResponse(DeviceStatus domain) {
        return DeviceStatusResponse.builder()
                .deviceId(domain.getDeviceId())
                .latestMeasurement(domain.getLatestMeasurement())
                .latestDate(domain.getLatestDate())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
