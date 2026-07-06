package com.collins.request_approval_service.dto.Response;

import com.collins.request_approval_service.enums.ApprovalDecision;
import com.collins.request_approval_service.enums.ApprovalLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApprovalResponse {

    private Long approvalId;
    private Long requestId;
    private Long approvedById;
    private String approverName;

    private ApprovalLevel approvalLevel;
    private ApprovalDecision decision;
    private String comment;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}
