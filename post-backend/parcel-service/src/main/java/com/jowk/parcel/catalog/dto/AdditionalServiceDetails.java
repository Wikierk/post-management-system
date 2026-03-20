package com.jowk.parcel.catalog.dto;

import com.jowk.parcel.catalog.entity.AdditionalService;
import java.math.BigDecimal;

public record AdditionalServiceDetails(

        Short id,
        String name,
        BigDecimal price

) {

    public static AdditionalServiceDetails fromEntity(
            AdditionalService additionalService) {
        return new AdditionalServiceDetails(
                additionalService.getId(),
                additionalService.getName(),
                additionalService.getPrice()
        );
    }

}
