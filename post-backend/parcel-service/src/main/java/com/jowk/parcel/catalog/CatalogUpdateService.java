package com.jowk.parcel.catalog;

import com.jowk.parcel.catalog.dto.*;

public interface CatalogUpdateService {

    ParcelTypeDetails createParcelType(CreateParcelTypeRequest request);
    ParcelTypeDetails updateParcelType(short typeId,
            UpdateParcelTypeRequest request);
    void archiveParcelType(short typeId);
    AdditionalServiceDetails createAdditionalService(
            CreateAdditionalServiceRequest request);
    AdditionalServiceDetails updateAdditionalService(
            short serviceId, UpdateAdditionalServiceRequest request);
    void archiveAdditionalService(short serviceId);

}
