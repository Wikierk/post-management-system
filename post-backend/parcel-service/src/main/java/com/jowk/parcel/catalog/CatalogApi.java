package com.jowk.parcel.catalog;

import com.jowk.parcel.catalog.dto.AdditionalServiceSummary;
import com.jowk.parcel.catalog.dto.ParcelTypeSummary;
import com.jowk.common.api.response.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel Catalog", description = "Public API for browsing " +
        "available parcel types and additional services")
@RequestMapping("/api/parcels")
public interface CatalogApi {

    @Operation(
            summary = "Get available parcel types",
            description = "Retrieves a list of all currently active " +
                    "parcel types available for ordering."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of available parcel types was successfully returned"
            ),
    })
    @GetMapping("/types")
    ResponseEntity<ListResponse<ParcelTypeSummary>> getParcelTypes();

    @Operation(
            summary = "Get available additional services",
            description = "Retrieves a list of all currently active additional " +
                    "services that a customer can add to their parcel."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of available additional services was successfully returned"
            ),
    })
    @GetMapping("/additional-services")
    ResponseEntity<ListResponse<AdditionalServiceSummary>> getAdditionalServices();

}