package com.jowk.parcel.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Request body for creating a new parcel type")
public record CreateParcelTypeRequest(

        @Schema(description = "Maximum allowed weight in kg", example = "20.50")
        @NotNull(message = "Max weight is required")
        @Positive(message = "Max weight must be greater than zero")
        BigDecimal maxWeight,

        @Schema(description = "Maximum allowed width in cm", example = "40")
        @NotNull(message = "Max width is required")
        @Positive(message = "Max width must be greater than zero")
        Short maxWidth,

        @Schema(description = "Maximum allowed height in cm", example = "40")
        @NotNull(message = "Max height is required")
        @Positive(message = "Max height must be greater than zero")
        Short maxHeight,

        @Schema(description = "Maximum allowed length in cm", example = "40")
        @NotNull(message = "Max length is required")
        @Positive(message = "Max length must be greater than zero")
        Short maxLength,

        @Schema(description = "Base price for this parcel type", example = "12.99")
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(description = "Short description of the parcel type", example = "Medium box")
        @NotBlank(message = "Description cannot be empty")
        String description

) { }