package com.jowk.parcel.payment;

import com.jowk.parcel.payment.dto.CreatePaymentResponse;
import java.util.UUID;

public interface PaymentService {
    CreatePaymentResponse createPayment(String trackingNumber, UUID clientId);
    void confirmPayment(UUID paymentId, UUID clientId);
}
