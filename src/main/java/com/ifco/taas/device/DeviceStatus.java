package com.ifco.taas.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatus {
    @Id
    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "latest_measurement")
    private Double latestMeasurement;

    @Column(name = "latest_date")
    private Instant latestDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
