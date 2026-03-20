package com.jowk.parcel.catalog;

import com.jowk.AggregateRepository;
import com.jowk.parcel.catalog.entity.ParcelType;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParcelTypeRepository extends
        AggregateRepository<ParcelType, Short> {

    List<ParcelType> findAllByIsAvailableTrue();

}
