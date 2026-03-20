package com.jowk.catalog;

import com.jowk.catalog.dto.*;

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
