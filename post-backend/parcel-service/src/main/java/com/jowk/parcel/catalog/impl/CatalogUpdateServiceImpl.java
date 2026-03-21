package com.jowk.parcel.catalog.impl;

import com.jowk.parcel.catalog.AdditionalServiceRepository;
import com.jowk.parcel.catalog.CatalogUpdateService;
import com.jowk.parcel.catalog.ParcelTypeRepository;
import com.jowk.parcel.catalog.dto.*;
import com.jowk.parcel.catalog.entity.AdditionalService;
import com.jowk.parcel.catalog.entity.ParcelType;
import com.jowk.common.api.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CatalogUpdateServiceImpl implements CatalogUpdateService {

    private final AdditionalServiceRepository additionalServiceRepository;
    private final ParcelTypeRepository parcelTypeRepository;

    @Override
    public ParcelTypeSummary createParcelType(CreateParcelTypeRequest request) {
        ParcelType type = new ParcelType();
        type.setMaxWeight(request.maxWeight());
        type.setMaxWidth(request.maxWidth());
        type.setMaxHeight(request.maxHeight());
        type.setMaxLength(request.maxLength());
        type.setPrice(request.price());
        type.setDescription(request.description());
        type.setAvailable(true);
        ParcelType createdType = parcelTypeRepository.save(type);
        return ParcelTypeSummary.fromEntity(createdType);
    }

    @Override
    public ParcelTypeSummary updateParcelType(
            short typeId, UpdateParcelTypeRequest request) {
        ParcelType type = parcelTypeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parcel type of given ID was not found."));
        if (request.maxWeight() != null) type.setMaxWeight(request.maxWeight());
        if (request.maxWidth() != null) type.setMaxWidth(request.maxWidth());
        if (request.maxHeight() != null) type.setMaxHeight(request.maxHeight());
        if (request.maxLength() != null) type.setMaxLength(request.maxLength());
        if (request.price() != null) type.setPrice(request.price());
        if (request.description() != null) type.setDescription(request.description());
        return ParcelTypeSummary.fromEntity(type);
    }

    @Override
    public void archiveParcelType(short typeId) {
        ParcelType type = parcelTypeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parcel type of given ID was not found."));
        type.setAvailable(false);
    }

    @Override
    public AdditionalServiceSummary createAdditionalService(
            CreateAdditionalServiceRequest request) {
        AdditionalService service = new AdditionalService();
        service.setName(request.name());
        service.setPrice(request.price());
        service.setAvailable(true);
        AdditionalService createdService = additionalServiceRepository.save(service);
        return AdditionalServiceSummary.fromEntity(createdService);
    }

    @Override
    public AdditionalServiceSummary updateAdditionalService(
            short serviceId, UpdateAdditionalServiceRequest request) {
        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Additional service of given ID was not found."));
        if (request.name() != null) service.setName(request.name());
        if (request.price() != null) service.setPrice(request.price());
        return AdditionalServiceSummary.fromEntity(service);
    }

    @Override
    public void archiveAdditionalService(short serviceId) {
        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Additional service of given ID was not found."));
        service.setAvailable(false);
    }

}
