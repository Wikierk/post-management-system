package com.jowk.user.branch.dto;

import com.jowk.user.branch.entity.Address;

public record AddressSummary(

        String city,
        String street,
        String number,
        String zipCode

) {

    public static AddressSummary fromEntity(Address address) {
        return new AddressSummary(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getZipCode()
        );
    }

}
