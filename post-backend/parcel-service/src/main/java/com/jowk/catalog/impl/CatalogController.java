package com.jowk.catalog.impl;

import com.jowk.catalog.CatalogApi;
import com.jowk.catalog.CatalogReadService;
import com.jowk.catalog.dto.AdditionalServiceDetails;
import com.jowk.catalog.dto.ParcelTypeDetails;
import com.jowk.common.api.ListResponse;
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
