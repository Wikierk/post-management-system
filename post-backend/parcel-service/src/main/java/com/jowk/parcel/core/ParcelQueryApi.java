package com.jowk.parcel.core;

import com.jowk.common.api.response.ListResponse;
import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.dto.ParcelSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel Queries", description = "Queries for parcels")
@RequestMapping("/api/parcels")
public interface ParcelQueryApi {

    @Operation(summary = "Get parcels of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned")
    })
    @GetMapping("/my")
    ResponseEntity<ListResponse<ParcelSummary>> getMyParcels(
            @AuthenticationPrincipal AuthenticatedUser user
    );

}
