package com.jowk.parcel.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Shared request body for parcel status changes")
public record ParcelStatusChangeRequest(

        @Schema(description = "Optional user note stored in parcel history",
                example = "Recipient refused acceptance due to damaged packaging")
        @Size(max = 500, message = "Description cannot exceed 500 characters.")
        String description

) { }

