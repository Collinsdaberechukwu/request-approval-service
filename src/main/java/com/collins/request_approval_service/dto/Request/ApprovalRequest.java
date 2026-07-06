package com.collins.request_approval_service.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ApprovalRequest {

    @NotNull(message = "Approver Id is required")
    @Positive(message = "Approver Id must be greater than zero")
    private Long approverId;

    @NotBlank(message = "Comment is required")
    private String comment;
}
