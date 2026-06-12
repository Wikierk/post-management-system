package com.jowk.parcel.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record CreateParcelRequest(

        @NotNull @Valid ParcelSubjectRequest sender,
        @NotNull @Valid ParcelSubjectRequest recipient,
        @NotNull Short parcelTypeId,
        @Positive BigDecimal cashOnDelivery,
        Set<Short> selectedServiceIds

) { }