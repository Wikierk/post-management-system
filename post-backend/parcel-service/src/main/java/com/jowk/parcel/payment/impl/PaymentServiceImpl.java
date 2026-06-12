package com.jowk.parcel.payment.impl;

import com.jowk.common.domain.exception.EntityNotFoundException;
import com.jowk.common.domain.valueobject.Money;
import com.jowk.parcel.core.ParcelRepository;
import com.jowk.parcel.core.dto.ParcelCreationResponse;
import com.jowk.parcel.payment.Payment;
import com.jowk.parcel.payment.PaymentRepository;
import com.jowk.parcel.payment.PaymentService;
import com.jowk.parcel.payment.PaymentStatus;
import com.jowk.parcel.payment.dto.CreatePaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final com.jowk.parcel.core.ParcelRepository parcelRepository;
    private final com.jowk.parcel.core.ParcelService parcelService;

    @Override
    public CreatePaymentResponse createPayment(String trackingNumber, UUID clientId) {
        var parcel = parcelRepository.findById(trackingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Parcel not found"));

        Money amount = parcel.getTotalPrice();
        Payment payment = new Payment(trackingNumber, amount);
        Payment saved = paymentRepository.save(payment);

        // Simulated checkout URL (in real integration redirect to provider)
        String checkoutUrl = "/payments/mock/" + saved.getId();

        return new CreatePaymentResponse(saved.getId().toString(), checkoutUrl);
    }

    @Override
    public void confirmPayment(UUID paymentId, UUID clientId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            return;
        }

        payment.markPaid();
        paymentRepository.save(payment);

        parcelService.markAsPaid(payment.getTrackingNumber(), clientId, null);
    }

}
