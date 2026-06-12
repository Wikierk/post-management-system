package com.jowk.parcel.payment;

import com.jowk.common.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount"))
    private Money amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public Payment(String trackingNumber, Money amount) {
        this.trackingNumber = trackingNumber;
        this.amount = amount;
        this.status = PaymentStatus.CREATED;
        this.createdAt = OffsetDateTime.now();
    }

    public void markPaid() {
        this.status = PaymentStatus.PAID;
    }

}
