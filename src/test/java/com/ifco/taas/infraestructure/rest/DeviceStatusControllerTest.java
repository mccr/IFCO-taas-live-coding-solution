package com.ifco.taas.infraestructure.rest;

import com.ifco.taas.application.usecase.GetDeviceStatusUseCase;
import com.ifco.taas.domain.DeviceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DeviceStatusController.class)
public class DeviceStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetDeviceStatusUseCase useCase;

    @Test
    void shouldGetAllDeviceStatusesSuccessfully() throws Exception {
        DeviceStatus deviceStatus1 = DeviceStatus.builder()
                .deviceId("1")
                .latestMeasurement(25.5)
                .latestDate(Instant.parse("2025-01-31T13:00:00Z"))
                .updatedAt(LocalDateTime.now())
                .build();
        DeviceStatus deviceStatus2 = DeviceStatus.builder()
                .deviceId("2")
                .latestMeasurement(10.4)
                .latestDate(Instant.parse("2025-01-30T11:00:00Z"))
                .updatedAt(LocalDateTime.now())
                .build();
        List<DeviceStatus> statuses = List.of(deviceStatus1, deviceStatus2);

        when(useCase.getAllDeviceStatuses()).thenReturn(statuses);

        mockMvc.perform(get("/device-status")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].deviceId").value("1"))
                .andExpect(jsonPath("$[0].latestMeasurement").value(25.5))
                .andExpect(jsonPath("$[1].deviceId").value("2"))
                .andExpect(jsonPath("$[1].latestMeasurement").value(10.4));
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
        when(useCase.getAllDeviceStatuses())
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/device-status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error reading latest statuses - Database connection failed"));
    }
}
