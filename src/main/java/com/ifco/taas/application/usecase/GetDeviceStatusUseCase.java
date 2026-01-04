package com.ifco.taas.application.usecase;

import com.ifco.taas.domain.DeviceStatus;

import java.util.List;

public interface GetDeviceStatusUseCase {
    List<DeviceStatus> getAllDeviceStatuses();
}
