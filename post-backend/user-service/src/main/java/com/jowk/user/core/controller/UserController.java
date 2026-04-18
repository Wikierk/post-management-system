package com.jowk.user.core.controller;

import com.jowk.user.core.User;
import com.jowk.user.core.UserRepository;
import com.jowk.user.core.dto.UserProfileResponse;
import com.jowk.common.security.domain.AuthenticatedUser;
import com.jowk.common.security.domain.AuthenticateEmployee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for user profile data")
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "Get current logged in user profile with roles")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        UUID userId = extractUserId(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus().name(),
                roles
        );

        return ResponseEntity.ok(response);
    }

    private UUID extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) principal).getId();
        } else if (principal instanceof AuthenticateEmployee) {
            return ((AuthenticateEmployee) principal).getId();
        }
        throw new IllegalStateException("Invalid principal type");
    }
}