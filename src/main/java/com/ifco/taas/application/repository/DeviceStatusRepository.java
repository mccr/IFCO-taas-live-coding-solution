package com.ifco.taas.application.repository;

import com.ifco.taas.domain.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, String> {
}
