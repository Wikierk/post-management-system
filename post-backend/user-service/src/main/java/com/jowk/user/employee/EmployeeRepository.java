package com.jowk.user.employee;

import com.jowk.user.core.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByUserIdAndUserStatus(UUID userId, UserStatus status);

    @EntityGraph(attributePaths = {"user", "branch"})
    Optional<Employee> findWithBranchAndUserByUserIdAndUserStatus(
            UUID userId, UserStatus status);

    Page<Employee> findByBranchId(UUID branchId, Pageable pageable);

}
