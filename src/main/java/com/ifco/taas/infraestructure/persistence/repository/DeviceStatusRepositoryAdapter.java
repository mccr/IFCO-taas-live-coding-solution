package com.ifco.taas.infraestructure.persistence.repository;

import com.ifco.taas.application.repository.DeviceStatusRepository;
import com.ifco.taas.domain.DeviceStatus;
import com.ifco.taas.infraestructure.persistence.entity.DeviceStatusEntity;
import com.ifco.taas.infraestructure.persistence.mapper.DeviceStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeviceStatusRepositoryAdapter implements DeviceStatusRepository {
    private final DeviceStatusJpaRepository jpaRepository;
    private final DeviceStatusMapper mapper;

    @Override
    public DeviceStatus save(DeviceStatus deviceStatus) {
        DeviceStatusEntity entity = mapper.toEntity(deviceStatus);

        DeviceStatusEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DeviceStatus> findById(String deviceId) {
        Optional<DeviceStatusEntity> existingEntity = jpaRepository.findById(deviceId);

        return existingEntity.map(mapper::toDomain);
    }

    @Override
    public List<DeviceStatus> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
