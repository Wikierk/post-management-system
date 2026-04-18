package com.jowk.user.core.controller;

import com.jowk.user.core.User;
import com.jowk.user.core.dto.UpdateUserStatusRequest;
import com.jowk.user.core.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin User Management", description = "Endpoints for managing users by administrators")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "Get all users with pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminUserService.getAllUsers(pageable));
    }

    @Operation(summary = "Change user status (e.g. block/unblock)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> changeUserStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        adminUserService.changeUserStatus(userId, request.status());
        return ResponseEntity.noContent().build();
    }
}