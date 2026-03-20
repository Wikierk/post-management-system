package com.jowk.catalog;

import com.jowk.catalog.dto.*;
import com.jowk.common.api.ListResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Parcel Catalog Admin",
        description = "Management of parcel types and additional services")
@RequestMapping("/api/admin/parcels")
public interface CatalogAdminApi {

    @Operation(summary = "Get all parcel types",
            description = "Returns a full list of parcel types, including inactive/archived ones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Parcel types were successfully returned")
    })
    @GetMapping("/types")
    ResponseEntity<ListResponse<ParcelTypeDetails>> getParcelTypes();

    @Operation(summary = "Get all additional services",
            description = "Returns a full list of available services like insurance or express delivery.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Additional services were successfully returned")
    })
    @GetMapping("/additional-services")
    ResponseEntity<ListResponse<AdditionalServiceDetails>> getAdditionalServices();

    @Operation(summary = "Create a new parcel type",
            description = "Adds a new type of parcel to the system catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parcel type was successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden - you don't have permission to create types")
    })
    @PostMapping("/types")
    ResponseEntity<ParcelTypeDetails> createParcelType(
            @RequestBody @Valid CreateParcelTypeRequest request);

    @Operation(summary = "Update an existing parcel type",
            description = "Partially updates fields of a parcel type identified by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel type was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Parcel type not found")
    })
    @PatchMapping("/types/{typeId}")
    ResponseEntity<ParcelTypeDetails> updateParcelType(
            @PathVariable("typeId") Short typeId,
            @RequestBody @Valid UpdateParcelTypeRequest request
    );

    @Operation(summary = "Archive a parcel type",
            description = "Marks a parcel type as unavailable for customers. It remains in history.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel was successfully archived"),
            @ApiResponse(responseCode = "404", description = "Parcel type not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @PostMapping("/types/{typeId}/archive")
    ResponseEntity<Void> archiveParcelType(
            @PathVariable("typeId") Short typeId
    );

    @Operation(summary = "Create a new additional service",
            description = "Adds a new supplementary service (e.g., Weekend Delivery) to the catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service was successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @PostMapping("/additional-services")
    ResponseEntity<AdditionalServiceDetails> createAdditionalService(
            @RequestBody @Valid CreateAdditionalServiceRequest request);

    @Operation(summary = "Update an additional service",
            description = "Updates details like name or price of an existing additional service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PatchMapping("/additional-services/{serviceId}")
    ResponseEntity<AdditionalServiceDetails> updateAdditionalService(
            @PathVariable("serviceId") Short serviceId,
            @RequestBody @Valid UpdateAdditionalServiceRequest request
    );

    @Operation(summary = "Archive an additional service",
            description = "Sets an additional service as inactive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service was successfully archived"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PostMapping("/additional-services/{serviceId}/archive")
    ResponseEntity<Void> archiveAdditionalService(
            @PathVariable("serviceId") Short serviceId
    );

}