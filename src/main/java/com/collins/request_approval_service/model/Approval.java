package com.collins.request_approval_service.model;

import com.collins.request_approval_service.enums.ApprovalDecision;
import com.collins.request_approval_service.enums.ApprovalLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approvals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approval extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", nullable = false)
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_level", nullable = false)
    private ApprovalLevel approvalLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalDecision decision;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "approved_at", nullable = false)
    private LocalDateTime approvedAt;
}
