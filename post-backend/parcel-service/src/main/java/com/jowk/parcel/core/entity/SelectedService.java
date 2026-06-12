package com.jowk.parcel.core.entity;

import com.jowk.common.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "selected_services")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SelectedService {

    @Id
    @Column(name = "selected_service_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price")
    )
    private Money price;

    @Column(name = "additional_service_id")
        @EqualsAndHashCode.Include
    private Short additionalServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_number")
    private Parcel parcel;

    public SelectedService(String name, Money price, Short additionalServiceId) {
        this.name = requireNotBlank(name, "name cannot be blank");
        this.price = Objects.requireNonNull(price, "price cannot be null");
        this.additionalServiceId = requirePositive(additionalServiceId,
                "additionalServiceId must be positive");
    }

    void attachToParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    private String requireNotBlank(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
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

}
