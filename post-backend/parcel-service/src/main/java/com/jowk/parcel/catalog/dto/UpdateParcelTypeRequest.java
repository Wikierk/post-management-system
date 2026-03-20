package com.jowk.parcel.catalog.dto;

import java.math.BigDecimal;

public record UpdateParcelTypeRequest(

        BigDecimal maxWeight,
        Short maxWidth,
        Short maxHeight,
        Short maxLength,
        BigDecimal price,
        String description

) { }
