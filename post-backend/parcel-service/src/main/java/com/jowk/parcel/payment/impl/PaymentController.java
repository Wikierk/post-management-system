package com.jowk.parcel.payment.impl;

import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.payment.PaymentApi;
import com.jowk.parcel.payment.PaymentService;
import com.jowk.parcel.payment.dto.CreatePaymentRequest;
import com.jowk.parcel.payment.dto.CreatePaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CreatePaymentResponse> createCheckout(@AuthenticationPrincipal AuthenticatedUser user, CreatePaymentRequest request) {
        CreatePaymentResponse response = paymentService.createPayment(request.trackingNumber(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Void> confirmPayment(@AuthenticationPrincipal AuthenticatedUser user, UUID paymentId) {
        paymentService.confirmPayment(paymentId, user == null ? null : user.getId());
        return ResponseEntity.noContent().build();
    }

}
