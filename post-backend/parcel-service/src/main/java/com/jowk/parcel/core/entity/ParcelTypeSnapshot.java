package com.jowk.parcel.core.entity;

import com.jowk.common.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "parcel_type_snapshots")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
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

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price")
    )
    private Money price;

    @Column(name = "description")
    private String description;

    @Column(name = "parcel_type_id")
    private Short parcelTypeId;

    public ParcelTypeSnapshot(BigDecimal maxWeight, Short maxWidth,
            Short maxHeight, Short maxLength, Money price,
            String description, Short parcelTypeId) {
        this.maxWeight = requirePositive(maxWeight, "maxWeight must be positive");
        this.maxWidth = requirePositive(maxWidth, "maxWidth must be positive");
        this.maxHeight = requirePositive(maxHeight, "maxHeight must be positive");
        this.maxLength = requirePositive(maxLength, "maxLength must be positive");
        this.price = Objects.requireNonNull(price, "price cannot be null");
        this.description = normalizeOptional(description);
        this.parcelTypeId = requirePositive(parcelTypeId, "parcelTypeId must be positive");
    }

    private BigDecimal requirePositive(BigDecimal value, String message) {
        Objects.requireNonNull(value, message);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private Short requirePositive(Short value, String message) {
        Objects.requireNonNull(value, message);
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

}
