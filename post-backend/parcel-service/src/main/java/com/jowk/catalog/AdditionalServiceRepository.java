package com.jowk.catalog;

import com.jowk.AggregateRepository;
import com.jowk.catalog.entity.AdditionalService;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdditionalServiceRepository extends
        AggregateRepository<AdditionalService, Short> {

    List<AdditionalService> findAllByIsAvailableTrue();

}
