package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.infraestructure.persistence.entity.DeviceStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceStatusJpaRepository extends JpaRepository<DeviceStatusEntity, String> {
}
