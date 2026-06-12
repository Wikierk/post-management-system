package com.jowk.parcel.core;

import com.jowk.common.domain.AggregateRepository;
import com.jowk.parcel.core.entity.Parcel;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelRepository extends
                AggregateRepository<Parcel, String> {

        java.util.List<Parcel> findAllBySenderUserId(java.util.UUID userId);

}


