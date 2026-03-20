package com.jowk.parcel.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Request body for partial update of a parcel type. " +
        "All fields are optional.")
public record UpdateParcelTypeRequest(

        @Schema(description = "New maximum weight in kg", example = "25.00")
        @Positive(message = "Max weight must be greater than zero")
        BigDecimal maxWeight,

        @Schema(description = "New maximum width in cm", example = "50")
        @Positive(message = "Max width must be greater than zero")
        Short maxWidth,

        @Schema(description = "New maximum height in cm", example = "50")
        @Positive(message = "Max height must be greater than zero")
        Short maxHeight,

        @Schema(description = "New maximum length in cm", example = "50")
        @Positive(message = "Max length must be greater than zero")
        Short maxLength,

        @Schema(description = "New base price", example = "19.99")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(description = "New description", example = "Updated description")
        @Size(min = 1, max = 50, message = "Description cannot be empty if provided")
        String description

) { }