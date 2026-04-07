package com.jowk.parcel.core;

import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import java.util.UUID;

public interface ParcelService {

    void markAsPaid(String trackingNumber, UUID clientId, ParcelStatusChangeRequest request);
    void markAsReceivedAtPostOffice(String trackingNumber, UUID clerkId,
            UUID branchId, ParcelStatusChangeRequest request);
    void markAsReceivedAtWarehouse(String trackingNumber, UUID warehousemanId,
            UUID branchId, ParcelStatusChangeRequest request);
    void markAsInTransit(String trackingNumber, UUID clerkId,
            UUID courierId, ParcelStatusChangeRequest request);
    void markAsAvailableForPickup(String trackingNumber, UUID clerkId,
            UUID branchId, ParcelStatusChangeRequest request);
    void markAsOutForDelivery(String trackingNumber, UUID clerkId,
            UUID courierId, ParcelStatusChangeRequest request);
    void markAsDelivered(String trackingNumber,
            UUID courierId, ParcelStatusChangeRequest request);
    void markAsDeliveryAttempted(String trackingNumber, UUID courierId,
            ParcelStatusChangeRequest request);
    void markAsNotAcceptedByRecipient(String trackingNumber, UUID courierId,
            ParcelStatusChangeRequest request);

}
