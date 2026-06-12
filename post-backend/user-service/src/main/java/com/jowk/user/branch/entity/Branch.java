package com.jowk.user.branch.entity;

import com.jowk.common.domain.AggregateRoot;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "branches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Branch implements AggregateRoot {

    @Id
    @Column(name = "branch_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "branch_type")
    @Enumerated(value = EnumType.STRING)
    private BranchType type;

    @Column(name = "is_active")
    private boolean isActive = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Version
    @Column(name = "version")
    private Long version;

    public Branch(BranchType type, Address address) {
        this.type = type;
        this.address = address;
        this.isActive = true;
    }

    public void deactivate() {
        if (!this.isActive) {
            throw new IllegalStateException("Branch is already inactive.");
        }
        this.isActive = false;
    }

    public void activate() {
        if (this.isActive) {
            throw new IllegalStateException("Branch is already active.");
        }
        this.isActive = true;
    }

    public void changeAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalArgumentException("Address cannot be null.");
        }
        this.address = newAddress;
    }

}
