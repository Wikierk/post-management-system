package com.jowk.parcel.catalog.impl;

import com.jowk.common.domain.valueobject.Money;
import com.jowk.parcel.catalog.AdditionalServiceRepository;
import com.jowk.parcel.catalog.CatalogUpdateService;
import com.jowk.parcel.catalog.ParcelTypeRepository;
import com.jowk.parcel.catalog.dto.*;
import com.jowk.parcel.catalog.entity.AdditionalService;
import com.jowk.parcel.catalog.entity.ParcelType;
import com.jowk.common.domain.exception.EntityNotFoundException;
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
        ParcelType type = new ParcelType(request.maxWeight(), request.maxWidth(),
                request.maxHeight(), request.maxLength(), Money.of(request.price()),
                request.description());
        ParcelType createdType = parcelTypeRepository.save(type);
        return ParcelTypeSummary.fromEntity(createdType);
    }

    @Override
    public ParcelTypeSummary updateParcelType(
            short typeId, UpdateParcelTypeRequest request) {
        ParcelType type = parcelTypeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parcel type of given ID was not found."));
        if (request.maxWeight() != null) type.changeMaxWeight(request.maxWeight());
        if (request.maxWidth() != null) type.changeMaxWidth(request.maxWidth());
        if (request.maxHeight() != null) type.changeMaxHeight(request.maxHeight());
        if (request.maxLength() != null) type.changeMaxLength(request.maxLength());
        if (request.price() != null) type.changePrice(Money.of(request.price()));
        if (request.description() != null) type.changeDescription(request.description());
        return ParcelTypeSummary.fromEntity(type);
    }

    @Override
    public void archiveParcelType(short typeId) {
        ParcelType type = parcelTypeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parcel type of given ID was not found."));
        type.disable();
    }

    @Override
    public AdditionalServiceSummary createAdditionalService(
            CreateAdditionalServiceRequest request) {
        AdditionalService service = new AdditionalService(
                request.name(), Money.of(request.price()));
        AdditionalService createdService = additionalServiceRepository.save(service);
        return AdditionalServiceSummary.fromEntity(createdService);
    }

    @Override
    public AdditionalServiceSummary updateAdditionalService(
            short serviceId, UpdateAdditionalServiceRequest request) {
        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Additional service of given ID was not found."));
        if (request.name() != null) service.changeName(request.name());
        if (request.price() != null) service.changePrice(Money.of(request.price()));
        return AdditionalServiceSummary.fromEntity(service);
    }

    @Override
    public void archiveAdditionalService(short serviceId) {
        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Additional service of given ID was not found."));
        service.disable();
    }

}
