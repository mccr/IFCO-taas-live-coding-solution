package com.ifco.taas.device;

import com.ifco.taas.application.service.DeviceStatusService;
import com.ifco.taas.infraestructure.rest.DeviceStatusController;
import com.ifco.taas.infraestructure.rest.dto.DeviceStatusResponse;
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
    private DeviceStatusService service;

    @Test
    void shouldGetAllDeviceStatusesSuccessfully() throws Exception {
        DeviceStatusResponse deviceStatus1 = DeviceStatusResponse.builder()
                .deviceId("1")
                .latestMeasurement(25.5)
                .latestDate(Instant.parse("2025-01-31T13:00:00Z"))
                .updatedAt(LocalDateTime.now())
                .build();
        DeviceStatusResponse deviceStatus2 = DeviceStatusResponse.builder()
                .deviceId("2")
                .latestMeasurement(10.4)
                .latestDate(Instant.parse("2025-01-30T11:00:00Z"))
                .updatedAt(LocalDateTime.now())
                .build();
        List<DeviceStatusResponse> statuses = List.of(deviceStatus1, deviceStatus2);

        when(service.getAllDeviceStatuses()).thenReturn(statuses);

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
        when(service.getAllDeviceStatuses())
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/device-status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error reading latest statuses - Database connection failed"));
    }
}
