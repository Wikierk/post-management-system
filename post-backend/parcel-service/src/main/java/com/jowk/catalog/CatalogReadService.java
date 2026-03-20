package com.jowk.catalog;

import com.jowk.catalog.dto.AdditionalServiceDetails;
import com.jowk.catalog.dto.ParcelTypeDetails;
import java.util.List;

public interface CatalogReadService {

    List<ParcelTypeDetails> getAvailableParcelTypes();
    List<AdditionalServiceDetails> getAvailableAdditionalServices();
    List<ParcelTypeDetails> getParcelTypes();
    List<AdditionalServiceDetails> getAdditionalServices();

}
