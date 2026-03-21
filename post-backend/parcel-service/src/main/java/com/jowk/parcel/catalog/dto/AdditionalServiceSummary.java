package com.jowk.parcel.catalog.dto;

import com.jowk.parcel.catalog.entity.AdditionalService;
import java.math.BigDecimal;

public record AdditionalServiceSummary(

        Short id,
        String name,
        BigDecimal price

) {

    public static AdditionalServiceSummary fromEntity(
            AdditionalService additionalService) {
        return new AdditionalServiceSummary(
                additionalService.getId(),
                additionalService.getName(),
                additionalService.getPrice()
        );
    }

}
