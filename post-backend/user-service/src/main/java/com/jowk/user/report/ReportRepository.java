package com.jowk.user.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    Page<Report> findByAssignedAdminId(UUID assignedAdminId, Pageable pageable);
    Page<Report> findByAuthorId(UUID authorId, Pageable pageable);

}
