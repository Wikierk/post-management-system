package com.jowk.user.branch.controller;

import com.jowk.user.branch.dto.BranchDetails;
import com.jowk.user.branch.dto.CreateBranchRequest;
import com.jowk.user.branch.service.AdminBranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v3/admin/branches")
@RequiredArgsConstructor
@Tag(name = "Admin Branch Management", description = "Endpoints for managing post offices and warehouses")
public class AdminBranchController {

    private final AdminBranchService adminBranchService;

    @Operation(summary = "Create a new branch (Post Office or Warehouse)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BranchDetails> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        BranchDetails created = adminBranchService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get list of all branches with pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BranchDetails>> getAllBranches(Pageable pageable) {
        return ResponseEntity.ok(adminBranchService.getAllBranches(pageable));
    }

    @Operation(summary = "Get specific branch details")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchDetails> getBranchById(@PathVariable UUID branchId) {
        return ResponseEntity.ok(adminBranchService.getBranchById(branchId));
    }

    @Operation(summary = "Update branch address")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{branchId}/address")
    public ResponseEntity<BranchDetails> updateBranchAddress(
            @PathVariable UUID branchId,
            @Valid @RequestBody CreateBranchRequest.Address address) {
        return ResponseEntity.ok(adminBranchService.updateBranchAddress(branchId, address));
    }

    @Operation(summary = "Deactivate a branch (Soft Delete)")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{branchId}/deactivate")
    public ResponseEntity<Void> deactivateBranch(@PathVariable UUID branchId) {
        adminBranchService.deactivateBranch(branchId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate a branch")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{branchId}/activate")
    public ResponseEntity<Void> activateBranch(@PathVariable UUID branchId) {
        adminBranchService.activateBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}