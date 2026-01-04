package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.application.repository.TelemetryRepository;
import com.ifco.taas.domain.Telemetry;
import com.ifco.taas.infraestructure.persistence.entity.TelemetryEntity;
import com.ifco.taas.infraestructure.persistence.mapper.TelemetryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelemetryRepositoryAdapter implements TelemetryRepository {
    private final TelemetryJpaRepository jpaRepository;
    private final TelemetryMapper mapper;

    @Override
    public Telemetry save(Telemetry telemetry) {
        TelemetryEntity entity = mapper.toEntity(telemetry);

        TelemetryEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}
