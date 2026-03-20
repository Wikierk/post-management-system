package com.jowk.parcel.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Schema(description = "Request to create a new additional service (e.g., Insurance, Express)")
public record CreateAdditionalServiceRequest(

        @Schema(description = "Name of the service", example = "Insurance")
        @NotBlank(message = "Service name is required")
        String name,

        @Schema(description = "Price for the service", example = "5.00")
        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price

) { }