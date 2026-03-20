package com.jowk.parcel.catalog.impl;

import com.jowk.parcel.catalog.AdditionalServiceRepository;
import com.jowk.parcel.catalog.CatalogReadService;
import com.jowk.parcel.catalog.ParcelTypeRepository;
import com.jowk.parcel.catalog.dto.AdditionalServiceDetails;
import com.jowk.parcel.catalog.dto.ParcelTypeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogReadServiceImpl implements CatalogReadService {

    private final AdditionalServiceRepository additionalServiceRepository;
    private final ParcelTypeRepository parcelTypeRepository;

    @Override
    public List<ParcelTypeDetails> getAvailableParcelTypes() {
        return parcelTypeRepository.findAllByIsAvailableTrue()
                .stream()
                .map(ParcelTypeDetails::fromEntity)
                .toList();
    }

    @Override
    public List<AdditionalServiceDetails> getAvailableAdditionalServices() {
        return additionalServiceRepository.findAllByIsAvailableTrue()
                .stream()
                .map(AdditionalServiceDetails::fromEntity)
                .toList();
    }

    @Override
    public List<ParcelTypeDetails> getParcelTypes() {
        return parcelTypeRepository.findAll()
                .stream()
                .map(ParcelTypeDetails::fromEntity)
                .toList();
    }

    @Override
    public List<AdditionalServiceDetails> getAdditionalServices() {
        return additionalServiceRepository.findAll()
                .stream()
                .map(AdditionalServiceDetails::fromEntity)
                .toList();
    }

}
