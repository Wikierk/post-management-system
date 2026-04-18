package com.jowk.user.core.dto;

import com.jowk.user.core.User;
import com.jowk.user.core.UserStatus;
import java.util.Set;
import java.util.UUID;

public record UserAdminResponse(
    UUID id,
    String email,
    String firstName,
    String lastName,
    UserStatus status,
    Set<String> roles
) {
    public static UserAdminResponse fromEntity(User user, Set<String> roles) {
        return new UserAdminResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getStatus(),
            roles
        );
    }
}
