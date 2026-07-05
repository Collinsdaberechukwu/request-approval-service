package com.collins.request_approval_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Department {

    ENGINEERING("Engineering"),

    FINANCE("Finance"),

    HR("Human_Resource_Management"),

    OPERATIONS("Operations"),

    SALES("Sales");

    private final String userDepartment;
}
