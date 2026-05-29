package com.jowk.user.core.dto;

import java.util.Set;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String status,
        Set<String> roles
) {}