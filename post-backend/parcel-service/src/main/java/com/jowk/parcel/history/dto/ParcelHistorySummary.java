package com.jowk.parcel.history.dto;

import com.jowk.parcel.core.entity.ParcelStatus;
import com.jowk.parcel.history.entity.ParcelHistory;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ParcelHistorySummary(
        UUID id,
        String trackingNumber,
        ParcelStatus status,
        String description,
        OffsetDateTime createdAt
) {

    public static ParcelHistorySummary fromEntity(ParcelHistory parcelHistory) {
        return new ParcelHistorySummary(parcelHistory.getId(),
                parcelHistory.getTrackingNumber(),
                parcelHistory.getStatus(),
                parcelHistory.getDescription(),
                parcelHistory.getCreatedAt());
    }

}
