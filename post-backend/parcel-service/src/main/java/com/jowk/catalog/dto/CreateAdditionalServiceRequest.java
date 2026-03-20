package com.jowk.catalog.dto;

import java.math.BigDecimal;

public record CreateAdditionalServiceRequest(

        String name,
        BigDecimal price

) { }
