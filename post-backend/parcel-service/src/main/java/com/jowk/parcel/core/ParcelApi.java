package com.jowk.parcel.core;

import com.jowk.common.security.domain.AuthenticateEmployee;
import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.dto.DispatchToCourierRequest;
import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel Commands",
        description = "API for handling parcel operations")
@RequestMapping("/api/parcels/{parcelId}")
public interface ParcelApi {

    @Operation(summary = "Register payment event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as paid"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/paid")
    ResponseEntity<Void> markAsPaid(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register received at post office event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as received at post office"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/received-at-post-office")
    ResponseEntity<Void> markAsReceivedAtPostOffice(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee clerk,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register received at warehouse event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as received at warehouse"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/received-at-warehouse")
    ResponseEntity<Void> markAsReceivedAtWarehouse(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee warehouseman,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register in transit event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as in transit"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/in-transit")
    ResponseEntity<Void> markAsInTransit(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee employee,
            @RequestBody @Valid DispatchToCourierRequest request);

    @Operation(summary = "Register available for pickup event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as available for pickup"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/available-for-pickup")
    ResponseEntity<Void> markAsAvailableForPickup(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee clerk,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register out for delivery event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as out for delivery"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/out-for-delivery")
    ResponseEntity<Void> markAsOutForDelivery(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee clerk,
            @RequestBody @Valid DispatchToCourierRequest request);

    @Operation(summary = "Register delivered event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as delivered"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/delivered")
    ResponseEntity<Void> markAsDelivered(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee courier,
            @RequestBody @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register delivery attempted event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as delivery attempted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/delivery-attempted")
    ResponseEntity<Void> markAsDeliveryAttempted(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee courier,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

    @Operation(summary = "Register not accepted by recipient event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parcel marked as not accepted by recipient"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Parcel not found"),
            @ApiResponse(responseCode = "409", description = "Illegal status transition")
    })
    @PostMapping("/not-accepted-by-recipient")
    ResponseEntity<Void> markAsNotAcceptedByRecipient(
            @PathVariable("parcelId") String parcelId,
            @AuthenticationPrincipal AuthenticateEmployee courier,
            @RequestBody(required = false) @Valid ParcelStatusChangeRequest request);

}

