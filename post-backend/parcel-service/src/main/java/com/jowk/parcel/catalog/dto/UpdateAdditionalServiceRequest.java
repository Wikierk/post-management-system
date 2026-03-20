package com.jowk.parcel.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Request to partially update an additional service")
public record UpdateAdditionalServiceRequest(

        @Schema(description = "New name of the service", example = "Premium Insurance")
        @Size(min = 1, message = "Name cannot be empty if provided")
        String name,

        @Schema(description = "New price for the service", example = "7.50")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price

) { }
