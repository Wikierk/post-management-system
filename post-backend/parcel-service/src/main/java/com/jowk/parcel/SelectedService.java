package com.jowk.parcel;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "selected_services")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SelectedService {

    @Id
    @Column(name = "selected_service_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "additional_service_id")
    private Short additionalServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_number")
    private Parcel parcel;

}
