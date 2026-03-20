package com.jowk.parcel.catalog.dto;

import com.jowk.parcel.catalog.entity.ParcelType;
import java.math.BigDecimal;

public record ParcelTypeDetails(

        Short id,
        BigDecimal maxWeight,
        Short maxWidth,
        Short maxHeight,
        Short maxLength,
        BigDecimal price,
        String description

) {

    public static ParcelTypeDetails fromEntity(ParcelType parcelType) {
        return new ParcelTypeDetails(parcelType.getId(), parcelType.getMaxWeight(),
                parcelType.getMaxWidth(), parcelType.getMaxHeight(),
                parcelType.getMaxLength(), parcelType.getPrice(),
                parcelType.getDescription());
    }

}
