package com.collins.request_approval_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalLevel {

    MANAGER("manager"),

    DEPARTMENT_HEAD("Head_Of_Department"),

    FINANCE("Finance"),

    HR("Human_Resource_Managment"),

    COMPLETED("Completed");

    private final String approvalLevel;
}
