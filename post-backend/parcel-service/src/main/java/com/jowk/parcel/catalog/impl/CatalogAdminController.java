package com.jowk.parcel.catalog.impl;

import com.jowk.parcel.catalog.CatalogAdminApi;
import com.jowk.parcel.catalog.CatalogReadService;
import com.jowk.parcel.catalog.CatalogUpdateService;
import com.jowk.parcel.catalog.dto.*;
import com.jowk.common.api.response.ListResponse;
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
    public ResponseEntity<ParcelTypeSummary> createParcelType(
            CreateParcelTypeRequest request) {
        ParcelTypeSummary parcelType = updateService.createParcelType(request);
        return ResponseEntity.ok(parcelType);
    }

    @Override
    public ResponseEntity<ParcelTypeSummary> updateParcelType(
            Short typeId, UpdateParcelTypeRequest request) {
        ParcelTypeSummary parcelType = updateService
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
    public ResponseEntity<AdditionalServiceSummary> createAdditionalService(
            CreateAdditionalServiceRequest request) {
        AdditionalServiceSummary additionalService = updateService
                .createAdditionalService(request);
        return ResponseEntity.ok(additionalService);
    }

    @Override
    public ResponseEntity<AdditionalServiceSummary> updateAdditionalService(
            Short serviceId, UpdateAdditionalServiceRequest request) {
        AdditionalServiceSummary additionalService = updateService
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
