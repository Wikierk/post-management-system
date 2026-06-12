package com.jowk.parcel.core;

import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.dto.CreateParcelRequest;
import com.jowk.parcel.core.dto.ParcelCreationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel Creation", description = "API for creating new parcels")
@RequestMapping("/api/parcels")
public interface ParcelCreationApi {

    @Operation(summary = "Create a new parcel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parcel was created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parcel data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    ResponseEntity<ParcelCreationResponse> createParcel(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid CreateParcelRequest request);

}