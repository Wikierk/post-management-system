package com.jowk.parcel.core.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "parcel_subjects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParcelSubject {

    @Id
    @Column(name = "parcel_subject_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id")
    private UUID userId;

    public ParcelSubject(String fullName, String street, String city,
            String zipCode, String email, String phone) {
        this(fullName, street, city, zipCode, email, phone, null);
    }

    public ParcelSubject(String fullName, String street, String city,
            String zipCode, String email, String phone, UUID userId) {
        this.fullName = requireNotBlank(fullName, "fullName cannot be blank");
        this.street = requireNotBlank(street, "street cannot be blank");
        this.city = requireNotBlank(city, "city cannot be blank");
        this.zipCode = requireNotBlank(zipCode, "zipCode cannot be blank");
        this.email = normalizeOptional(email);
        this.phone = normalizeOptional(phone);
        this.userId = userId;
    }

    private String requireNotBlank(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

}
