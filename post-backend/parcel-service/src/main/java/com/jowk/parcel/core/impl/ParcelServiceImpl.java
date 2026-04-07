package com.jowk.parcel.core.impl;

import com.jowk.common.domain.exception.EntityNotFoundException;
import com.jowk.parcel.core.ParcelRepository;
import com.jowk.parcel.core.ParcelService;
import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import com.jowk.parcel.core.entity.Parcel;
import com.jowk.parcel.history.entity.LogisticHolder;
import com.jowk.parcel.history.entity.LogisticHolderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;

    @Override
    public void markAsPaid(String trackingNumber, UUID clientId,
            ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsPaid(descriptionOf(request), clientId,
                new LogisticHolder(clientId, LogisticHolderType.CLIENT));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsReceivedAtPostOffice(String trackingNumber, UUID clerkId,
            UUID branchId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsReceivedAtPostOffice(descriptionOf(request),
                clerkId, new LogisticHolder(branchId, LogisticHolderType.POST_OFFICE));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsReceivedAtWarehouse(String trackingNumber, UUID warehousemanId,
            UUID branchId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsReceivedAtWarehouse(descriptionOf(request),
                warehousemanId, new LogisticHolder(branchId, LogisticHolderType.WAREHOUSE));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsInTransit(String trackingNumber, UUID clerkId,
            UUID courierId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsInTransit(descriptionOf(request), clerkId,
                new LogisticHolder(courierId, LogisticHolderType.COURIER));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsAvailableForPickup(String trackingNumber, UUID clerkId,
            UUID branchId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsAvailableForPickup(descriptionOf(request),
                clerkId, new LogisticHolder(branchId, LogisticHolderType.POST_OFFICE));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsOutForDelivery(String trackingNumber, UUID clerkId,
            UUID courierId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsOutForDelivery(descriptionOf(request),
                clerkId, new LogisticHolder(courierId, LogisticHolderType.COURIER));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsDelivered(String trackingNumber,
            UUID courierId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsDelivered(descriptionOf(request), courierId);
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsDeliveryAttempted(String trackingNumber,
            UUID courierId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsDeliveryAttempted(descriptionOf(request),
                courierId, new LogisticHolder(courierId, LogisticHolderType.COURIER));
        parcelRepository.save(parcel);
    }

    @Override
    public void markAsNotAcceptedByRecipient(String trackingNumber,
            UUID courierId, ParcelStatusChangeRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsNotAcceptedByRecipient(descriptionOf(request),
                courierId, new LogisticHolder(courierId, LogisticHolderType.COURIER));
        parcelRepository.save(parcel);
    }

    private Parcel getParcelOrThrow(String trackingNumber) {
        return parcelRepository.findById(trackingNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Parcel with given tracking number was not found."));
    }

    private String descriptionOf(ParcelStatusChangeRequest request) {
        return request == null ? null : request.description();
    }

}
