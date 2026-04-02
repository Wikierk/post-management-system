package com.jowk.parcel.history.dto;

import com.jowk.parcel.core.entity.ParcelStatus;
import com.jowk.parcel.history.entity.LogisticHolder;
import java.util.UUID;

public record ParcelStatusChangedEvent(
        String trackingNumber,
        ParcelStatus status,
        String description,
        UUID actorId,
        LogisticHolder logisticHolder
) { }
