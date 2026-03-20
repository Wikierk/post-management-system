package com.jowk.catalog;

import com.jowk.catalog.dto.AdditionalServiceDetails;
import com.jowk.catalog.dto.ParcelTypeDetails;
import com.jowk.common.api.ListResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Parcel")
@RequestMapping("/api/parcels")
public interface CatalogApi {

    @GetMapping("/types")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Parcel types were successfully returned")
    })
    ResponseEntity<ListResponse<ParcelTypeDetails>> getParcelTypes();

    @GetMapping("/additional-services")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Additional services were successfully returned"),
    })
    ResponseEntity<ListResponse<AdditionalServiceDetails>> getAdditionalServices();

}
