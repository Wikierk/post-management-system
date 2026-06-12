package com.jowk.parcel.core.dto;

import jakarta.validation.constraints.NotBlank;

public record ParcelSubjectRequest(

        @NotBlank String fullName,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String zipCode,
        String email,
        String phone

) { }