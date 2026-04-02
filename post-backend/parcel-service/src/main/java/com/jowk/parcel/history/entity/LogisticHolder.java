package com.jowk.parcel.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LogisticHolder {

    @Column(name = "logistic_holder_id")
    private UUID id;

    @Column(name = "logistic_holder_type")
    @Enumerated(EnumType.STRING)
    private LogisticHolderType type;

    public LogisticHolder(UUID id, LogisticHolderType type) {
        validateId(id);
        validateType(type);
        this.id = id;
        this.type = type;
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Logistic holder ID cannot be null.");
        }
    }

    private void validateType(LogisticHolderType type) {
        if (type == null) {
            throw new IllegalArgumentException("Logistic holder type cannot be null.");
        }
    }

}
