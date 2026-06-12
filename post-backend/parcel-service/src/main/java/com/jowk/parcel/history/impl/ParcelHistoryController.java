package com.jowk.parcel.history.impl;

import com.jowk.common.api.response.ListResponse;
import com.jowk.parcel.history.ParcelHistoryApi;
import com.jowk.parcel.history.ParcelHistoryService;
import com.jowk.parcel.history.dto.ParcelHistorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParcelHistoryController implements ParcelHistoryApi {

    private final ParcelHistoryService parcelHistoryService;

    @Override
    public ResponseEntity<ListResponse<ParcelHistorySummary>> getParcelHistory(
            String trackingNumber) {
        return ResponseEntity.ok(ListResponse.of(
                parcelHistoryService.getLatestHistory(trackingNumber)));
    }
}