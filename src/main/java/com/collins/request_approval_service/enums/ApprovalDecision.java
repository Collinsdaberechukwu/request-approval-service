package com.collins.request_approval_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalDecision {

    APPROVED("Approved"),

    REJECTED("Rejected");

    private final String approvalDecision;
}
