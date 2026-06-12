package com.jowk.parcel.core.entity;

import com.jowk.common.domain.AggregateRoot;
import com.jowk.common.domain.valueobject.Money;
import com.jowk.parcel.history.entity.LogisticHolder;
import com.jowk.parcel.history.entity.LogisticHolderType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import com.jowk.parcel.history.dto.ParcelStatusChangedEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "parcels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Parcel extends AbstractAggregateRoot<Parcel>
        implements Persistable<String>, AggregateRoot {

    @Id
    @Column(name = "tracking_number")
    @EqualsAndHashCode.Include
    private String trackingNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "total_price")
    )
    private Money totalPrice;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "cash_on_delivery")
    )
    private Money cashOnDelivery;

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

    @Version
    @Column(name = "version")
    private Long version;

    @Transient
    private boolean isNew = true;

    public Parcel(Money cashOnDelivery, ParcelSubject sender,
            ParcelSubject recipient, ParcelTypeSnapshot parcelType,
            Collection<SelectedService> selectedServices) {
        this.trackingNumber = UUID.randomUUID().toString();
        this.status = ParcelStatus.REGISTERED;
        this.cashOnDelivery = cashOnDelivery;
        this.sender = requireNonNull(sender, "sender cannot be null");
        this.recipient = requireNonNull(recipient, "recipient cannot be null");
        this.parcelType = requireNonNull(parcelType, "parcelType cannot be null");
        this.selectedServices = toSelectedServicesSet(selectedServices);
        this.selectedServices.forEach(selectedService -> selectedService.attachToParcel(this));
        this.totalPrice = calculateTotalPrice(this.parcelType, this.selectedServices);
    }

    public void registerAsCreated(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        requireNonNull(actorId, "actorId cannot be null");
        requireNonNull(logisticHolder, "logisticHolder cannot be null");
        registerEvent(new ParcelStatusChangedEvent(this.trackingNumber,
                this.status, normalizeDescription(description), actorId, logisticHolder));
    }

    public void markAsPaid(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.PAID, description, actorId,
                logisticHolder, ParcelStatus.REGISTERED);
    }

    public void markAsReceivedAtPostOffice(String description,
            UUID actorId, LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.RECEIVED_AT_POST_OFFICE,
                description, actorId, logisticHolder,
                ParcelStatus.PAID);
    }

    public void markAsReceivedAtWarehouse(String description,
            UUID actorId, LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.RECEIVED_AT_WAREHOUSE,
                description, actorId, logisticHolder,
                ParcelStatus.IN_TRANSIT);
    }

    public void markAsInTransit(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.IN_TRANSIT, description, actorId,
                logisticHolder, ParcelStatus.RECEIVED_AT_WAREHOUSE,
                ParcelStatus.RECEIVED_AT_POST_OFFICE);
    }

    public void markAsAvailableForPickup(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.AVAILABLE_FOR_PICKUP,
                description, actorId, logisticHolder,
                ParcelStatus.IN_TRANSIT);
    }

    public void markAsOutForDelivery(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.OUT_FOR_DELIVERY, description, actorId, logisticHolder,
                ParcelStatus.AVAILABLE_FOR_PICKUP);
    }

    public void markAsDelivered(String description, UUID actorId) {
        transitionTo(ParcelStatus.DELIVERED, description, actorId,
                new LogisticHolder(null, LogisticHolderType.RECIPIENT),
                ParcelStatus.OUT_FOR_DELIVERY);
    }

    public void markAsDeliveryAttempted(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.DELIVERY_ATTEMPTED, description,
                actorId, logisticHolder, ParcelStatus.OUT_FOR_DELIVERY);
    }

    public void markAsNotAcceptedByRecipient(String description, UUID actorId,
            LogisticHolder logisticHolder) {
        transitionTo(ParcelStatus.NOT_ACCEPTED_BY_RECIPIENT, description,
                actorId, logisticHolder, ParcelStatus.OUT_FOR_DELIVERY);
    }

    @Override
    public String getId() {
        return trackingNumber;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    private void transitionTo(ParcelStatus targetStatus, String description, UUID actorId,
            LogisticHolder logisticHolder, ParcelStatus... allowedCurrentStatuses) {
        requireNonNull(targetStatus, "targetStatus cannot be null");
        requireNonNull(actorId, "actorId cannot be null");
        requireNonNull(logisticHolder, "logisticHolder cannot be null");
        boolean transitionAllowed = false;
        for (ParcelStatus allowedStatus : allowedCurrentStatuses) {
            if (this.status == allowedStatus) {
                transitionAllowed = true;
                break;
            }
        }
        if (!transitionAllowed) {
            throw new IllegalStateException("Illegal parcel status transition from " +
                    this.status + " to " + targetStatus + ".");
        }
        this.status = targetStatus;
        registerEvent(new ParcelStatusChangedEvent(this.trackingNumber,
                this.status, normalizeDescription(description), actorId, logisticHolder));
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description;
    }

    private Set<SelectedService> toSelectedServicesSet(
            Collection<SelectedService> selectedServices) {
        if (selectedServices == null || selectedServices.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(selectedServices);
    }

    private Money calculateTotalPrice(ParcelTypeSnapshot parcelType,
            Collection<SelectedService> selectedServices) {
        Money basePrice = requireNonNull(parcelType.getPrice(),
                "parcelType price cannot be null");
        Money selectedServicesTotal = selectedServices.stream()
                .map(this::extractServicePrice)
                .reduce(Money.zero(), Money::plus);
        return basePrice.plus(selectedServicesTotal);
    }

    private Money extractServicePrice(SelectedService selectedService) {
        SelectedService service = requireNonNull(selectedService,
                "selectedService cannot be null");
        return requireNonNull(service.getPrice(),
                "selectedService price cannot be null");
    }

    private <T> T requireNonNull(T value, String message) {
        return Objects.requireNonNull(value, message);
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        isNew = false;
    }

}
