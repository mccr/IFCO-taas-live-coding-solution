package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.infraestructure.persistence.entity.TelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryJpaRepository extends JpaRepository<TelemetryEntity, Long> {
}
