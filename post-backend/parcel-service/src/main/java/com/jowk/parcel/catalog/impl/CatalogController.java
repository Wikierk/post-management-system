package com.jowk.parcel.catalog.impl;

import com.jowk.parcel.catalog.CatalogApi;
import com.jowk.parcel.catalog.CatalogReadService;
import com.jowk.parcel.catalog.dto.AdditionalServiceDetails;
import com.jowk.parcel.catalog.dto.ParcelTypeDetails;
import com.jowk.common.api.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogController implements CatalogApi {

    private final CatalogReadService readService;

    public ResponseEntity<ListResponse<ParcelTypeDetails>> getParcelTypes() {
        List<ParcelTypeDetails> parcelTypes = readService.getAvailableParcelTypes();
        return ResponseEntity.ok(ListResponse.of(parcelTypes));
    }

    public ResponseEntity<ListResponse<AdditionalServiceDetails>> getAdditionalServices() {
        List<AdditionalServiceDetails> additionalServices =
                readService.getAvailableAdditionalServices();
        return ResponseEntity.ok(ListResponse.of(additionalServices));
    }

}
