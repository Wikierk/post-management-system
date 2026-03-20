package com.jowk.catalog.impl;

import com.jowk.catalog.CatalogAdminApi;
import com.jowk.catalog.CatalogService;
import com.jowk.catalog.dto.AdditionalServiceDetails;
import com.jowk.catalog.dto.ParcelTypeDetails;
import com.jowk.common.api.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogAdminController implements CatalogAdminApi {

    private final CatalogService catalogService;

    @Override
    public ResponseEntity<ListResponse<ParcelTypeDetails>> getParcelTypes() {
        List<ParcelTypeDetails> parcelTypes = catalogService.getParcelTypes();
        return ResponseEntity.ok(ListResponse.of(parcelTypes));
    }

    @Override
    public ResponseEntity<ListResponse<AdditionalServiceDetails>> getAdditionalServices() {
        List<AdditionalServiceDetails> additionalServices =
                catalogService.getAdditionalServices();
        return ResponseEntity.ok(ListResponse.of(additionalServices));
    }

}
