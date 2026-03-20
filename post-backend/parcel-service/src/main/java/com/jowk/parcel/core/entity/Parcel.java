package com.jowk.parcel.core.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parcels")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Parcel implements Persistable<String> {

    @Id
    @Column(name = "tracking_number")
    @EqualsAndHashCode.Include
    private String trackingNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "cash_on_delivery")
    private BigDecimal cashOnDelivery;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sender_id")
    private ParcelSubject sender;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "recipient_id")
    private ParcelSubject recipient;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parcel_type_snapshot_id")
    private ParcelTypeSnapshot parcelType;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SelectedService> selectedServices = new HashSet<>();

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private Set<ParcelHistory> history = new HashSet<>();

    @Transient
    private boolean isNew = true;

    @Override
    public String getId() {
        return trackingNumber;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        isNew = false;
    }

}

