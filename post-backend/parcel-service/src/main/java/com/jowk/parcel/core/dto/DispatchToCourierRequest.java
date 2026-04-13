package com.jowk.parcel.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "Request body for dispatching a parcel to a courier")
public record DispatchToCourierRequest(

        @Schema(description = "The unique identifier of the courier assigned to the parcel",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Courier ID is mandatory for dispatching.")
        UUID courierId,

        @Schema(description = "Optional note regarding the dispatch process",
                example = "Parcel handed over to morning shift courier")
        @Size(max = 500, message = "Description cannot exceed 500 characters.")
        String description

) { }