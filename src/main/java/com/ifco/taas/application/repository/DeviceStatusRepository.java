package com.ifco.taas.application.repository;

import com.ifco.taas.domain.DeviceStatus;

import java.util.List;
import java.util.Optional;

public interface DeviceStatusRepository {
    DeviceStatus save(DeviceStatus deviceStatus);

    Optional<DeviceStatus> findById(String deviceId);

    List<DeviceStatus> findAll();
}
