package com.jowk.user.branch.dto;

import com.jowk.user.branch.entity.BranchType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a new branch")
public record CreateBranchRequest(

        @Schema(description = "Type of the branch", example = "POST_OFFICE")
        @NotNull(message = "Branch type is required")
        BranchType type,

        @Schema(description = "Address of the branch")
        @NotNull(message = "Address is required")
        @Valid
        Address address

) {

    @Schema(description = "Address information")
    public record Address(

            @Schema(description = "City name", example = "Warsaw")
            @NotBlank(message = "City is required")
            String city,

            @Schema(description = "Street name", example = "Main Street")
            @NotBlank(message = "Street is required")
            String street,

            @Schema(description = "Building/house number", example = "123")
            @NotBlank(message = "Building number is required")
            String number,

            @Schema(description = "Postal code", example = "00-001")
            @NotBlank(message = "Zip code is required")
            String zipCode

    ) { }

}


