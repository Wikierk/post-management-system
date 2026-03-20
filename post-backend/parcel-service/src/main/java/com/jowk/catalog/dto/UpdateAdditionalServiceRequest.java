package com.jowk.catalog.dto;

import java.math.BigDecimal;

public record UpdateAdditionalServiceRequest(

        String name,
        BigDecimal price

) { }
