package com.jowk.user.core.dto;

import com.jowk.user.core.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update user status by Admin")
public record UpdateUserStatusRequest(

        @Schema(description = "New status for the user", example = "LOCKED")
        @NotNull(message = "Status is required")
        UserStatus status

) {
}