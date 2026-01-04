package com.ifco.taas.infraestructure.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryRequest {
    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @NotNull(message = "Measurement is required")
    private Double measurement;

    @NotBlank(message = "Date is required")
    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z",
            message = "Date must be in ISO-8601 format (e.g., 2025-01-31T13:00:00Z)"
    )
    private String date;
}
