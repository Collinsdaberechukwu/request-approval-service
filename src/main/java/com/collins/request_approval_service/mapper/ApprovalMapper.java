package com.collins.request_approval_service.mapper;

import com.collins.request_approval_service.dto.Response.ApprovalResponse;
import com.collins.request_approval_service.model.Approval;
import org.springframework.stereotype.Component;

@Component
public class ApprovalMapper {

    public ApprovalResponse mapToResponse(Approval approval){

        return ApprovalResponse.builder()
                .approvalId(approval.getId())
                .requestId(approval.getRequest().getId())
                .approvedById(approval.getApprovedBy().getId())
                .approverName(
                        approval.getApprovedBy().getFirstName()
                                + " "
                                + approval.getApprovedBy().getLastName())
                .approvalLevel(approval.getApprovalLevel())
                .decision(approval.getDecision())
                .comment(approval.getComment())
                .approvedAt(approval.getApprovedAt())
                .createdAt(approval.getCreatedAt())
                .build();
    }
}
