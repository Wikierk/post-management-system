package com.jowk.parcel.catalog.impl;

import com.jowk.parcel.catalog.CatalogApi;
import com.jowk.parcel.catalog.CatalogReadService;
import com.jowk.parcel.catalog.dto.AdditionalServiceSummary;
import com.jowk.parcel.catalog.dto.ParcelTypeSummary;
import com.jowk.common.api.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogController implements CatalogApi {

    private final CatalogReadService readService;

    public ResponseEntity<ListResponse<ParcelTypeSummary>> getParcelTypes() {
        List<ParcelTypeSummary> parcelTypes = readService.getAvailableParcelTypes();
        return ResponseEntity.ok(ListResponse.of(parcelTypes));
    }

    public ResponseEntity<ListResponse<AdditionalServiceSummary>> getAdditionalServices() {
        List<AdditionalServiceSummary> additionalServices =
                readService.getAvailableAdditionalServices();
        return ResponseEntity.ok(ListResponse.of(additionalServices));
    }

}
