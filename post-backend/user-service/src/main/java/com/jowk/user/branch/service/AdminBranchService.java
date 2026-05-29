package com.jowk.user.branch.service;

import com.jowk.user.branch.dto.BranchDetails;
import com.jowk.user.branch.dto.CreateBranchRequest;
import com.jowk.user.branch.entity.Address;
import com.jowk.user.branch.entity.Branch;
import com.jowk.user.branch.entity.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBranchService {

    private final BranchRepository branchRepository;

    @Transactional
    public BranchDetails createBranch(CreateBranchRequest request) {
        Address address = new Address(
                request.address().city(),
                request.address().street(),
                request.address().number(),
                request.address().zipCode()
        );

        Branch branch = new Branch(request.type(), address);
        Branch savedBranch = branchRepository.save(branch);

        log.info("Admin created new branch of type {} with ID {}", savedBranch.getType(), savedBranch.getId());
        return BranchDetails.fromEntity(savedBranch);
    }

    @Transactional(readOnly = true)
    public Page<BranchDetails> getAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable)
                .map(BranchDetails::fromEntity);
    }

    @Transactional(readOnly = true)
    public BranchDetails getBranchById(UUID branchId) {
        return branchRepository.findById(branchId)
                .map(BranchDetails::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));
    }

    @Transactional
    public BranchDetails updateBranchAddress(UUID branchId, CreateBranchRequest.Address addressDto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));

        Address newAddress = new Address(
                addressDto.city(),
                addressDto.street(),
                addressDto.number(),
                addressDto.zipCode()
        );

        branch.changeAddress(newAddress);
        log.info("Admin updated address for branch {}", branchId);

        return BranchDetails.fromEntity(branch);
    }

    @Transactional
    public void deactivateBranch(UUID branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch with ID " + branchId + " not found"));

        branch.deactivate();
        log.info("Admin deactivated branch {}", branchId);
    }
}