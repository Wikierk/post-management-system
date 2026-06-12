package com.jowk.parcel.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentRequest(
        @NotBlank String trackingNumber
) { }
