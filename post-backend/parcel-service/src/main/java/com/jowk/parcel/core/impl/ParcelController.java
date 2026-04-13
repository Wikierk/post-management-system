package com.jowk.parcel.core.impl;

import com.jowk.common.security.domain.AuthenticateEmployee;
import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.ParcelApi;
import com.jowk.parcel.core.ParcelService;
import com.jowk.parcel.core.dto.DispatchToCourierRequest;
import com.jowk.parcel.core.dto.ParcelStatusChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParcelController implements ParcelApi {

    private final ParcelService parcelService;

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Void> markAsPaid(String parcelId,
            AuthenticatedUser user, ParcelStatusChangeRequest request) {
        parcelService.markAsPaid(parcelId, user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('CLERK')")
    public ResponseEntity<Void> markAsReceivedAtPostOffice(String parcelId,
            AuthenticateEmployee clerk, ParcelStatusChangeRequest request) {
        parcelService.markAsReceivedAtPostOffice(parcelId, clerk.getId(),
                clerk.getBranchId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('WAREHOUSEMAN')")
    public ResponseEntity<Void> markAsReceivedAtWarehouse(String parcelId,
            AuthenticateEmployee warehouseman, ParcelStatusChangeRequest request) {
        parcelService.markAsReceivedAtWarehouse(parcelId, warehouseman.getId(),
                warehouseman.getBranchId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('CLERK', 'WAREHOUSEMAN')")
    public ResponseEntity<Void> markAsInTransit(String parcelId,
            AuthenticateEmployee employee, DispatchToCourierRequest request) {
        parcelService.markAsInTransit(parcelId, employee.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('CLERK')")
    public ResponseEntity<Void> markAsAvailableForPickup(String parcelId,
            AuthenticateEmployee clerk, ParcelStatusChangeRequest request) {
        parcelService.markAsAvailableForPickup(parcelId, clerk.getId(),
                clerk.getBranchId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('CLERK')")
    public ResponseEntity<Void> markAsOutForDelivery(String parcelId,
            AuthenticateEmployee clerk, DispatchToCourierRequest request) {
        parcelService.markAsOutForDelivery(parcelId, clerk.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('COURIER')")
    public ResponseEntity<Void> markAsDelivered(String parcelId,
            AuthenticateEmployee courier, ParcelStatusChangeRequest request) {
        parcelService.markAsDelivered(parcelId, courier.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('COURIER')")
    public ResponseEntity<Void> markAsDeliveryAttempted(String parcelId,
            AuthenticateEmployee courier, ParcelStatusChangeRequest request) {
        parcelService.markAsDeliveryAttempted(parcelId, courier.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('COURIER')")
    public ResponseEntity<Void> markAsNotAcceptedByRecipient(String parcelId,
            AuthenticateEmployee courier, ParcelStatusChangeRequest request) {
        parcelService.markAsNotAcceptedByRecipient(parcelId, courier.getId(), request);
        return ResponseEntity.noContent().build();
    }

}
