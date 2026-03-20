package com.jowk.parcel.catalog.dto;

import java.math.BigDecimal;

public record CreateAdditionalServiceRequest(

        String name,
        BigDecimal price

) { }
