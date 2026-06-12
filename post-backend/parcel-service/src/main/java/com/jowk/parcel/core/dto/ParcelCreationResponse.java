package com.jowk.parcel.core.dto;

import java.math.BigDecimal;

public record ParcelCreationResponse(

        String trackingNumber,
        BigDecimal totalPrice

) { }