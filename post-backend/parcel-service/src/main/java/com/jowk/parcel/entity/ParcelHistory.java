package com.jowk.parcel.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "parcel_histories")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParcelHistory {

    @Id
    @Column(name = "parcel_history_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "actor_id")
    private UUID actorId;

    @Column(name = "logistic_holder_id")
    private UUID logisticHolderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_number")
    private Parcel parcel;

}
