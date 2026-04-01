package com.jowk.user.branch;

import com.jowk.common.domain.AggregateRepository;
import com.jowk.user.branch.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends AggregateRepository<Branch, UUID> {

    @EntityGraph(attributePaths = {"address"})
    Optional<Branch> findWithAddressById(UUID id);

    @EntityGraph(attributePaths = {"address"})
    @Query("SELECT b FROM Branch b")
    Page<Branch> findAllWithAddress(Pageable pageable);

}
