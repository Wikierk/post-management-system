package com.jowk.parcel.payment.dto;

public record CreatePaymentResponse(
        String paymentId,
        String checkoutUrl
) { }
