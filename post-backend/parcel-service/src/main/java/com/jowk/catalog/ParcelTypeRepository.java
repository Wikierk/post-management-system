package com.jowk.catalog;

import com.jowk.AggregateRepository;
import com.jowk.catalog.entity.ParcelType;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelTypeRepository extends
        AggregateRepository<ParcelType, Short> { }
