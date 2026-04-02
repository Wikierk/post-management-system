package com.jowk.parcel.history.impl;

import com.jowk.parcel.history.ParcelHistoryRepository;
import com.jowk.parcel.history.ParcelHistoryService;
import com.jowk.parcel.history.dto.ParcelHistorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParcelHistoryServiceImpl implements ParcelHistoryService {

    private final ParcelHistoryRepository parcelHistoryRepository;

    @Override
    public List<ParcelHistorySummary> getLatestHistory(String trackingNumber) {
        return parcelHistoryRepository.findByTrackingNumber(
                trackingNumber, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ParcelHistorySummary::fromEntity)
                .toList();
    }

}
