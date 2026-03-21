package com.jowk.parcel.catalog;

import com.jowk.parcel.catalog.dto.*;

public interface CatalogUpdateService {

    ParcelTypeSummary createParcelType(CreateParcelTypeRequest request);
    ParcelTypeSummary updateParcelType(short typeId,
            UpdateParcelTypeRequest request);
    void archiveParcelType(short typeId);
    AdditionalServiceSummary createAdditionalService(
            CreateAdditionalServiceRequest request);
    AdditionalServiceSummary updateAdditionalService(
            short serviceId, UpdateAdditionalServiceRequest request);
    void archiveAdditionalService(short serviceId);

}
