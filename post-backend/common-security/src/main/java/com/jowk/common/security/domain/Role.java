package com.jowk.common.security.domain;

public enum Role {

    CUSTOMER(false),
    COURIER(true),
    WAREHOUSEMAN(true),
    CLERK(true),
    ADMIN(true);

    private final boolean isEmployee;

    Role(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

}
