package com.ifco.taas.infraestructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifco.taas.application.usecase.RecordTelemetryUseCase;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.rest.dto.TelemetryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TelemetryController.class)
public class TelemetryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordTelemetryUseCase useCase;

    private String deviceId;
    private Double measurement;
    private String date;
    private TelemetryRequest request;

    @BeforeEach
    void setUp() {
        deviceId = "1";
        measurement = 25.5;
        date = "2025-01-31T13:00:00Z";

        request = TelemetryRequest.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(date)
                .build();
    }

    @Test
    void shouldRecordTelemetrySuccessfully() throws Exception {
        Telemetry telemetry = Telemetry.builder()
                .deviceId(deviceId)
                .measurement(measurement)
                .date(Instant.parse(date))
                .build();

        when(useCase.record(deviceId, measurement, Instant.parse(date))).thenReturn(telemetry);

        mockMvc.perform(post("/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("1"))
                .andExpect(jsonPath("$.measurement").value(25.5))
                .andExpect(jsonPath("$.date").value("2025-01-31T13:00:00Z"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        when(useCase.record(deviceId, measurement, Instant.parse(date)))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error recording telemetry - Database connection failed"));
    }

    @Test
    void shouldReturnBadRequestWhenDateFormatIsInvalid() throws Exception {
        request.setDate("2025-01-31");

        mockMvc.perform(post("/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDeviceIdIsEmpty() throws Exception {
        request.setDeviceId("");

        mockMvc.perform(post("/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenMeasurementIsNull() throws Exception {
        request.setMeasurement(null);

        mockMvc.perform(post("/telemetry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
