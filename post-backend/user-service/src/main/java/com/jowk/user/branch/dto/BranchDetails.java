package com.jowk.user.branch.dto;

import com.jowk.user.branch.entity.Branch;
import com.jowk.user.branch.entity.BranchType;
import java.util.UUID;

public record BranchDetails(
        UUID id,
        BranchType type,
        AddressSummary address
) {

    public static BranchDetails fromEntity(Branch branch) {
        return new BranchDetails(
                branch.getId(),
                branch.getType(),
                AddressSummary.fromEntity(branch.getAddress()));
    }

}
