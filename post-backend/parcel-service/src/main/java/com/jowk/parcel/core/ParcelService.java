package com.jowk.parcel.core;

import com.jowk.parcel.core.dto.DispatchToCourierRequest;
import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import org.springframework.lang.Nullable;
import java.util.UUID;

public interface ParcelService {

    void markAsPaid(String trackingNumber, UUID clientId,
            @Nullable ParcelStatusChangeRequest request);
    void markAsReceivedAtPostOffice(String trackingNumber, UUID clerkId,
            UUID branchId, @Nullable ParcelStatusChangeRequest request);
    void markAsReceivedAtWarehouse(String trackingNumber, UUID warehousemanId,
            UUID branchId, @Nullable ParcelStatusChangeRequest request);
    void markAsInTransit(String trackingNumber, UUID clerkId, DispatchToCourierRequest request);
    void markAsAvailableForPickup(String trackingNumber, UUID clerkId,
            UUID branchId, @Nullable ParcelStatusChangeRequest request);
    void markAsOutForDelivery(String trackingNumber, UUID clerkId,
            DispatchToCourierRequest request);
    void markAsDelivered(String trackingNumber, UUID courierId,
            @Nullable ParcelStatusChangeRequest request);
    void markAsDeliveryAttempted(String trackingNumber, UUID courierId,
            @Nullable ParcelStatusChangeRequest request);
    void markAsNotAcceptedByRecipient(String trackingNumber, UUID courierId,
            @Nullable ParcelStatusChangeRequest request);

}
