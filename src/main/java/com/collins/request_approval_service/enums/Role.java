package com.collins.request_approval_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    EMPLOYEE("Employee"),

    MANAGER("Manager"),

    DEPARTMENT_HEAD("Head_Of_Department"),

    FINANCE("Finance"),

    HR("Human_Resource_Management");

    private final String userRole;
}
