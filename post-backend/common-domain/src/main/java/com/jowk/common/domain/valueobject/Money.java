package com.jowk.common.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money implements Comparable<Money> {

    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = normalize(amount);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return ZERO;
    }

    public BigDecimal toBigDecimal() {
        return amount;
    }

    public Money plus(Money other) {
        requireNonNull(other, "Money to add cannot be null.");
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        requireNonNull(other, "Money to subtract cannot be null.");
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal multiplier) {
        requireNonNull(multiplier, "Multiplier cannot be null.");
        return new Money(amount.multiply(multiplier));
    }

    public Money divide(BigDecimal divisor) {
        requireNonNull(divisor, "Divisor cannot be null.");
        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            throw new IllegalArgumentException("Divisor cannot be zero.");
        }
        return new Money(amount.divide(divisor, SCALE, ROUNDING_MODE));
    }

    @Override
    public int compareTo(@NonNull Money other) {
        requireNonNull(other, "Money to compare cannot be null.");
        return amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Money money)) {
            return false;
        }
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }

    private static BigDecimal normalize(BigDecimal value) {
        requireNonNull(value, "Amount cannot be null.");
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    private static void requireNonNull(Object value, String message) {
        Objects.requireNonNull(value, message);
    }

}
