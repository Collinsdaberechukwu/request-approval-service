package com.collins.request_approval_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestStatus {

    PENDING("Pending"),

    IN_PROGRESS("In_Progress"),

    APPROVED("Approved"),

    REJECTED("Rejected");

    private final String requestStatus;
}
