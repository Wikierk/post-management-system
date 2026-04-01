package com.jowk.user.branch.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "zip_code")
    private String zipCode;

    public Address(String city, String street, String number, String zipCode) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }

}
