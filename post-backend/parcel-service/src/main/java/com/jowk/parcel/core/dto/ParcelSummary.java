package com.jowk.parcel.core.dto;

import com.jowk.parcel.core.entity.ParcelStatus;
import java.math.BigDecimal;

public record ParcelSummary(
        String trackingNumber,
        ParcelStatus status,
        BigDecimal totalPrice,
        BigDecimal cashOnDelivery
) { }
