package com.jowk.report;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Report {

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "assigned_admin_id")
    private UUID assignedAdminId;

    @Column(name = "author_id")
    private UUID authorId;

}
