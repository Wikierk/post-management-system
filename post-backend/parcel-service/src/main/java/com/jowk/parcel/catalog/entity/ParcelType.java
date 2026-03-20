package com.jowk.parcel.catalog.entity;

import com.jowk.AggregateRoot;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "parcel_types")
@Getter
@Setter
@NoArgsConstructor
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
    private Short maxWidth;

    @Column(name = "max_height")
    private Short maxHeight;

    @Column(name = "max_length")
    private Short maxLength;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private boolean isAvailable;

}
