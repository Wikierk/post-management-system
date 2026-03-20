package com.jowk.parcel.catalog.dto;

import java.math.BigDecimal;

public record UpdateAdditionalServiceRequest(

        String name,
        BigDecimal price

) { }
