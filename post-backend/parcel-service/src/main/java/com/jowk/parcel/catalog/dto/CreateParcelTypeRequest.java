package com.jowk.parcel.catalog.dto;

import java.math.BigDecimal;

public record CreateParcelTypeRequest(

        BigDecimal maxWeight,
        Short maxWidth,
        Short maxHeight,
        Short maxLength,
        BigDecimal price,
        String description

) { }
