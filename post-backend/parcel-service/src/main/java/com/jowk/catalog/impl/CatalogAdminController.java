package com.jowk.catalog.impl;

import com.jowk.catalog.CatalogAdminApi;
import com.jowk.catalog.CatalogReadService;
import com.jowk.catalog.CatalogUpdateService;
import com.jowk.catalog.dto.*;
import com.jowk.common.api.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogAdminController implements CatalogAdminApi {

    private final CatalogUpdateService updateService;
    private final CatalogReadService readService;

    @Override
    public ResponseEntity<ListResponse<ParcelTypeDetails>> getParcelTypes() {
        List<ParcelTypeDetails> parcelTypes = readService.getParcelTypes();
        return ResponseEntity.ok(ListResponse.of(parcelTypes));
    }

    @Override
    public ResponseEntity<ListResponse<AdditionalServiceDetails>> getAdditionalServices() {
        List<AdditionalServiceDetails> additionalServices =
                readService.getAdditionalServices();
        return ResponseEntity.ok(ListResponse.of(additionalServices));
    }

    @Override
    public ResponseEntity<ParcelTypeDetails> createParcelType(
            CreateParcelTypeRequest request) {
        ParcelTypeDetails parcelType = updateService.createParcelType(request);
        return ResponseEntity.ok(parcelType);
    }

    @Override
    public ResponseEntity<ParcelTypeDetails> updateParcelType(
            Short typeId, UpdateParcelTypeRequest request) {
        ParcelTypeDetails parcelType = updateService
                .updateParcelType(typeId, request);
        return ResponseEntity.ok(parcelType);
    }

    @Override
    public ResponseEntity<Void> archiveParcelType(Short typeId) {
        updateService.archiveParcelType(typeId);
        return ResponseEntity.notFound()
                .build();
    }

    @Override
    public ResponseEntity<AdditionalServiceDetails> createAdditionalService(
            CreateAdditionalServiceRequest request) {
        AdditionalServiceDetails additionalService = updateService
                .createAdditionalService(request);
        return ResponseEntity.ok(additionalService);
    }

    @Override
    public ResponseEntity<AdditionalServiceDetails> updateAdditionalService(
            Short serviceId, UpdateAdditionalServiceRequest request) {
        AdditionalServiceDetails additionalService = updateService
                .updateAdditionalService(serviceId, request);
        return ResponseEntity.ok(additionalService);
    }

    @Override
    public ResponseEntity<Void> archiveAdditionalService(Short serviceId) {
        updateService.archiveAdditionalService(serviceId);
        return ResponseEntity.noContent()
                .build();
    }

}
