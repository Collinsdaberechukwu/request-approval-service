package com.collins.request_approval_service.model;

import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request extends BaseEntity{


    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalLevel currentApprovalLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Builder.Default
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approval> approvals = new ArrayList<>();
}
