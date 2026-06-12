package com.jowk.parcel.core.impl;

import com.jowk.common.api.response.ListResponse;
import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.ParcelQueryApi;
import com.jowk.parcel.core.ParcelRepository;
import com.jowk.parcel.core.dto.ParcelSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ParcelQueryController implements ParcelQueryApi {

    private final ParcelRepository parcelRepository;

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ListResponse<ParcelSummary>> getMyParcels(AuthenticatedUser user) {
        UUID userId = user.getId();
        List<ParcelSummary> summaries = parcelRepository.findAllBySenderUserId(userId).stream()
                .map(p -> new ParcelSummary(
                        p.getTrackingNumber(),
                        p.getStatus(),
                        p.getTotalPrice() == null ? null : p.getTotalPrice().toBigDecimal(),
                        p.getCashOnDelivery() == null ? null : p.getCashOnDelivery().toBigDecimal()
                ))
                .toList();

        return ResponseEntity.ok(ListResponse.of(summaries));
    }

}
