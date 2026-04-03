package com.jowk.parcel.catalog.entity;

import com.jowk.common.domain.AggregateRoot;
import com.jowk.common.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parcel_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParcelType implements AggregateRoot {

    @Id
    @Column(name = "parcel_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Short id;

    @Column(name = "max_weight")
    private BigDecimal maxWeight;

    @Column(name = "max_width")
    private short maxWidth;

    @Column(name = "max_height")
    private short maxHeight;

    @Column(name = "max_length")
    private short maxLength;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price")
    )
    private Money price;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Version
    @Column(name = "version")
    private Long version;

    public ParcelType(BigDecimal maxWeight, short maxWidth, short maxHeight,
            short maxLength, Money price, String description) {
        validateMaxWeight(maxWeight);
        validateMaxWidth(maxWidth);
        validateMaxHeight(maxHeight);
        validateMaxLength(maxLength);
        validatePrice(price);
        validateDescription(description);
        this.maxWeight = maxWeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxLength = maxLength;
        this.price = price;
        this.description = description;
        this.isAvailable = true;
    }

    public void changeMaxWeight(BigDecimal newMaxWeight) {
        validateNotArchived();
        validateMaxWeight(newMaxWeight);
        this.maxWeight = newMaxWeight;
    }

    public void changeMaxWidth(Short newMaxWidth) {
        validateNotArchived();
        validateMaxWidth(newMaxWidth);
        this.maxWidth = newMaxWidth;
    }

    public void changeMaxHeight(Short newMaxHeight) {
        validateNotArchived();
        validateMaxHeight(newMaxHeight);
        this.maxHeight = newMaxHeight;
    }

    public void changeMaxLength(Short newMaxLength) {
        validateNotArchived();
        validateMaxLength(newMaxLength);
        this.maxLength = newMaxLength;
    }

    public void changePrice(Money newPrice) {
        validateNotArchived();
        validatePrice(newPrice);
        this.price = newPrice;
    }

    public void changeDescription(String newDescription) {
        validateNotArchived();
        validateDescription(newDescription);
        this.description = newDescription;
    }

    public void disable() {
        if (!this.isAvailable) {
            throw new IllegalStateException("Type is already archived.");
        }
        this.isAvailable = false;
    }

    private void validateNotArchived() {
        if (!this.isAvailable) {
            throw new IllegalStateException("Cannot modify archived parcel type.");
        }
    }

    private void validateMaxWeight(BigDecimal maxWeight) {
        if (maxWeight == null) {
            throw new IllegalArgumentException("Max weight cannot be null.");
        }
        if (maxWeight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max weight must be greater than zero.");
        }
    }

    private void validateMaxWidth(short maxWidth) {
        if (maxWidth <= 0) {
            throw new IllegalArgumentException("Max width must be greater than zero.");
        }
    }

    private void validateMaxHeight(short maxHeight) {
        if (maxHeight <= 0) {
            throw new IllegalArgumentException("Max height must be greater than zero.");
        }
    }

    private void validateMaxLength(short maxLength) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("Max length must be greater than zero.");
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

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
    }

}
