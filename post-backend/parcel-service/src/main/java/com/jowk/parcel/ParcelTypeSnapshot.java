package com.jowk.parcel;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "parcel_type_snapshots")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParcelTypeSnapshot {

    @Id
    @Column(name = "parcel_type_snapshot_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "max_weight")
    private BigDecimal maxWeight;

    @Column(name = "max_width")
    private Short maxWidth;

    @Column(name = "max_height")
    private Short maxHeight;

    @Column(name = "max_length")
    private Short maxLength;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "parcel_type_id")
    private Short parcelTypeId;

}
