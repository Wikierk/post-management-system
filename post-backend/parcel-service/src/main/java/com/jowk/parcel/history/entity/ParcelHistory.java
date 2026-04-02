package com.jowk.parcel.history.entity;

import com.jowk.common.domain.AggregateRoot;
import com.jowk.parcel.core.entity.ParcelStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "parcel_histories")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParcelHistory implements AggregateRoot {

    @Id
    @Column(name = "parcel_history_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "actor_id")
    private UUID actorId;

    @Embedded
    private LogisticHolder logisticHolder;

    public ParcelHistory(String trackingNumber, ParcelStatus status,
            String description, UUID actorId, LogisticHolder logisticHolder) {
        validateTrackingNumber(trackingNumber);
        validateLogisticHolder(logisticHolder);
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.description = description;
        this.createdAt = OffsetDateTime.now();
        this.actorId = actorId;
        this.logisticHolder = logisticHolder;
    }

    private void validateTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number cannot be null or empty.");
        }
    }

    private void validateLogisticHolder(LogisticHolder logisticHolder) {
        if (logisticHolder == null) {
            throw new IllegalArgumentException("Logistic holder cannot be null.");
        }
    }

}
