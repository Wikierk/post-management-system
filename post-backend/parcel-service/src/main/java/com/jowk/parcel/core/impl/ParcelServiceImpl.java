package com.jowk.parcel.core.impl;

import com.jowk.common.domain.exception.EntityNotFoundException;
import com.jowk.common.domain.valueobject.Money;
import com.jowk.parcel.core.ParcelRepository;
import com.jowk.parcel.core.ParcelService;
import com.jowk.parcel.core.dto.CreateParcelRequest;
import com.jowk.parcel.core.dto.DispatchToCourierRequest;
import com.jowk.parcel.core.dto.ParcelCreationResponse;
import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import com.jowk.parcel.core.entity.ParcelSubject;
import com.jowk.parcel.core.entity.ParcelTypeSnapshot;
import com.jowk.parcel.core.entity.SelectedService;
import com.jowk.parcel.catalog.CatalogReadService;
import com.jowk.parcel.catalog.dto.AdditionalServiceDetails;
import com.jowk.parcel.catalog.dto.ParcelTypeDetails;
import com.jowk.parcel.core.entity.Parcel;
import com.jowk.parcel.history.entity.LogisticHolder;
import com.jowk.parcel.history.entity.LogisticHolderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final CatalogReadService catalogReadService;

    @Override
    public ParcelCreationResponse createParcel(CreateParcelRequest request, UUID clientId) {
        ParcelTypeDetails parcelTypeDetails = getParcelTypeDetails(request.parcelTypeId());
        Collection<SelectedService> selectedServices = getSelectedServices(request.selectedServiceIds());

        ParcelSubject sender = new ParcelSubject(
                request.sender().fullName(),
                request.sender().street(),
                request.sender().city(),
                request.sender().zipCode(),
                request.sender().email(),
                request.sender().phone(),
                clientId
        );
        ParcelSubject recipient = new ParcelSubject(
                request.recipient().fullName(),
                request.recipient().street(),
                request.recipient().city(),
                request.recipient().zipCode(),
                request.recipient().email(),
                request.recipient().phone()
        );

        Parcel parcel = new Parcel(
                toMoney(request.cashOnDelivery()),
                sender,
                recipient,
                toParcelTypeSnapshot(parcelTypeDetails),
                selectedServices
        );
        parcel.registerAsCreated("Przesyłka nadana", clientId,
                new LogisticHolder(clientId, LogisticHolderType.CLIENT));

        Parcel savedParcel = parcelRepository.save(parcel);
        return new ParcelCreationResponse(savedParcel.getTrackingNumber(),
                savedParcel.getTotalPrice().toBigDecimal());
    }

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
    public void markAsInTransit(String trackingNumber,
            UUID clerkId, DispatchToCourierRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsInTransit(request.description(), clerkId,
                new LogisticHolder(request.courierId(), LogisticHolderType.COURIER));
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
            DispatchToCourierRequest request) {
        Parcel parcel = getParcelOrThrow(trackingNumber);
        parcel.markAsOutForDelivery(request.description(),
                clerkId, new LogisticHolder(
                        request.courierId(), LogisticHolderType.COURIER));
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

    private ParcelTypeDetails getParcelTypeDetails(Short parcelTypeId) {
        return catalogReadService.getParcelTypes().stream()
                .filter(parcelType -> parcelType.id().equals(parcelTypeId))
                .filter(ParcelTypeDetails::isAvailable)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Parcel type with given id was not found or is unavailable."));
    }

    private Collection<SelectedService> getSelectedServices(Set<Short> selectedServiceIds) {
        if (selectedServiceIds == null || selectedServiceIds.isEmpty()) {
            return Set.of();
        }

        Map<Short, AdditionalServiceDetails> serviceById = catalogReadService.getAdditionalServices().stream()
                .filter(AdditionalServiceDetails::isAvailable)
                .filter(service -> selectedServiceIds.contains(service.id()))
                .collect(Collectors.toMap(AdditionalServiceDetails::id, Function.identity()));

        if (serviceById.size() != selectedServiceIds.size()) {
            throw new IllegalArgumentException("One or more additional services are unavailable.");
        }

        return serviceById.values().stream()
                .map(service -> new SelectedService(service.name(), Money.of(service.price()), service.id()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    private ParcelTypeSnapshot toParcelTypeSnapshot(ParcelTypeDetails parcelTypeDetails) {
        return new ParcelTypeSnapshot(
                parcelTypeDetails.maxWeight(),
                parcelTypeDetails.maxWidth(),
                parcelTypeDetails.maxHeight(),
                parcelTypeDetails.maxLength(),
                Money.of(parcelTypeDetails.price()),
                parcelTypeDetails.description(),
                parcelTypeDetails.id()
        );
    }

    private Money toMoney(BigDecimal value) {
        return value == null ? null : Money.of(value);
    }

    private String descriptionOf(ParcelStatusChangeRequest request) {
        return request == null ? null : request.description();
    }

}
