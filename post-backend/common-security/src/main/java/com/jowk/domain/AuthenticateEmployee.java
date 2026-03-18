package com.jowk.domain;

import lombok.Getter;
import java.util.Set;
import java.util.UUID;

@Getter
public class AuthenticateEmployee extends AuthenticatedUser {

    private final UUID branchId;

    public AuthenticateEmployee(UUID id, String username, String password,
                                Set<Role> roles, boolean isEnabled, UUID branchId) {
        super(id, username, password, roles, isEnabled);
        this.branchId = branchId;
    }

}
