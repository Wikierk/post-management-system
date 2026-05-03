package com.jowk.parcel.catalog;

import com.jowk.parcel.catalog.dto.AdditionalServiceDetails;
import com.jowk.parcel.catalog.dto.AdditionalServiceSummary;
import com.jowk.parcel.catalog.dto.ParcelTypeDetails;
import com.jowk.parcel.catalog.dto.ParcelTypeSummary;
import java.util.List;
import java.util.Set;

public interface CatalogReadService {

    List<ParcelTypeSummary> getAvailableParcelTypes();
    List<AdditionalServiceSummary> getAvailableAdditionalServices();
    List<ParcelTypeDetails> getParcelTypes();
    List<AdditionalServiceDetails> getAdditionalServices();
    ParcelTypeSummary getParcelTypeById(Short id);
    List<AdditionalServiceSummary> getAdditionalServicesByIds(Set<Short> id);

}
