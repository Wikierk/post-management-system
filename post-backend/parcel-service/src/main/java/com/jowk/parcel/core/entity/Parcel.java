package com.jowk.parcel.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parcels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Parcel extends AbstractAggregateRoot<Parcel> implements Persistable<String> {

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

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.PERSIST)
    private Set<SelectedService> selectedServices = new HashSet<>();

    @Transient
    private boolean isNew = true;

    public Parcel(String trackingNumber, ParcelStatus status,
                  BigDecimal totalPrice, BigDecimal cashOnDelivery,
                  ParcelSubject sender, ParcelSubject recipient,
                  ParcelTypeSnapshot parcelType, Collection<SelectedService> selectedServices) {
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.cashOnDelivery = cashOnDelivery;
        this.sender = sender;
        this.recipient = recipient;
        this.parcelType = parcelType;
        this.selectedServices = new HashSet<>(selectedServices);
    }

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
