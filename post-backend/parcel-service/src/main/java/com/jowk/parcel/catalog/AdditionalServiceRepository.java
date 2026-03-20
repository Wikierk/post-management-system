package com.jowk.parcel.catalog;

import com.jowk.common.domain.AggregateRepository;
import com.jowk.parcel.catalog.entity.AdditionalService;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdditionalServiceRepository extends
        AggregateRepository<AdditionalService, Short> {

    List<AdditionalService> findAllByIsAvailableTrue();

}
