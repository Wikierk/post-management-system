package com.jowk.parcel.catalog;

import com.jowk.parcel.catalog.dto.AdditionalServiceDetails;
import com.jowk.parcel.catalog.dto.ParcelTypeDetails;
import java.util.List;

public interface CatalogReadService {

    List<ParcelTypeDetails> getAvailableParcelTypes();
    List<AdditionalServiceDetails> getAvailableAdditionalServices();
    List<ParcelTypeDetails> getParcelTypes();
    List<AdditionalServiceDetails> getAdditionalServices();

}
