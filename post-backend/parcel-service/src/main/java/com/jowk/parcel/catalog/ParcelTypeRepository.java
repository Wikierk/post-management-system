package com.jowk.parcel.catalog;

import com.jowk.common.domain.AggregateRepository;
import com.jowk.parcel.catalog.entity.ParcelType;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelTypeRepository extends
        AggregateRepository<ParcelType, Short> {

    List<ParcelType> findAllByIsAvailableTrue();
    Optional<ParcelType> findByIdAndIsAvailableTrue(Short id);

}
