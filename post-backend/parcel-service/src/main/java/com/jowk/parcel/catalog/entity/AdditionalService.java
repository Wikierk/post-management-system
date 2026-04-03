package com.jowk.parcel.catalog.entity;

import com.jowk.common.domain.AggregateRoot;
import com.jowk.common.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "additional_services")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdditionalService implements AggregateRoot {

    @Id
    @Column(name = "additional_service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Short id;

    @Column(name = "name")
    private String name;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price")
    )
    private Money price;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Version
    @Column(name = "version")
    private Long version;

    public AdditionalService(String name, Money price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.isAvailable = true;
    }

    public void changeName(String newName) {
        validateNotArchived();
        validateName(newName);
        this.name = newName;
    }

    public void changePrice(Money newPrice) {
        validateNotArchived();
        validatePrice(newPrice);
        this.price = newPrice;
    }

    public void disable() {
        if (!this.isAvailable) {
            throw new IllegalStateException("Additional service is already disabled.");
        }
        this.isAvailable = false;
    }

    private void validateNotArchived() {
        if (!this.isAvailable) {
            throw new IllegalStateException("Cannot modify archived additional service.");
        }
    }

    private void validatePrice(Money price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null.");
        }
        if (price.compareTo(Money.zero()) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
    }

}
