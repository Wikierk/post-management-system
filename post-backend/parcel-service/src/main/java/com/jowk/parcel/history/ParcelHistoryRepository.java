package com.jowk.parcel.history;

import com.jowk.common.domain.AggregateRepository;
import com.jowk.parcel.history.entity.ParcelHistory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ParcelHistoryRepository extends
        AggregateRepository<ParcelHistory, UUID> {

    List<ParcelHistory> findByTrackingNumber(String trackingNumber, Sort sort);

}
