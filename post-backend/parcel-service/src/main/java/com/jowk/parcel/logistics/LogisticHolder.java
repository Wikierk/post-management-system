package com.jowk.parcel.logistics;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.util.UUID;

@Entity
@Table(name = "logistic_holders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LogisticHolder implements Persistable<UUID> {

    @Id
    @Column(name = "logistic_holder_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "logistic_holder_type")
    @Enumerated(EnumType.STRING)
    private LogisticHolderType type;

    @Transient
    private boolean isNew = true;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        isNew = false;
    }

}
