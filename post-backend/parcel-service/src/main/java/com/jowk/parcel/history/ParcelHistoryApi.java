package com.jowk.parcel.history;

import com.jowk.common.api.response.ListResponse;
import com.jowk.parcel.history.dto.ParcelHistorySummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel Tracking", description = "Public API for parcel status history")
@RequestMapping("/api/parcels/{trackingNumber}/history")
public interface ParcelHistoryApi {

    @Operation(
            summary = "Get parcel status history",
            description = "Returns the latest status history entries for a parcel tracking number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel history returned successfully"),
            @ApiResponse(responseCode = "404", description = "Parcel not found")
    })
    @GetMapping
    ResponseEntity<ListResponse<ParcelHistorySummary>> getParcelHistory(
            @PathVariable("trackingNumber") String trackingNumber);
}