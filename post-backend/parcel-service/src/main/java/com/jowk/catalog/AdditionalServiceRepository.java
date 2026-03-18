package com.jowk.catalog;

import com.jowk.AggregateRepository;
import com.jowk.catalog.entity.AdditionalService;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceRepository extends
        AggregateRepository<AdditionalService, Short> { }
