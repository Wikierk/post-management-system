package com.jowk.parcel.core.impl;

import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.parcel.core.ParcelCreationApi;
import com.jowk.parcel.core.ParcelService;
import com.jowk.parcel.core.dto.CreateParcelRequest;
import com.jowk.parcel.core.dto.ParcelCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParcelCreationController implements ParcelCreationApi {

    private final ParcelService parcelService;

    @Override
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ParcelCreationResponse> createParcel(AuthenticatedUser user,
            CreateParcelRequest request) {
        ParcelCreationResponse response = parcelService.createParcel(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}