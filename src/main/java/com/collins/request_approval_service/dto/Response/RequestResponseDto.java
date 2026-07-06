package com.collins.request_approval_service.dto.Response;

import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.enums.Department;
import com.collins.request_approval_service.enums.RequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponseDto {

    private Long id;
    private String title;
    private String description;
    private RequestStatus requestStatus;
    private ApprovalLevel currentApprovalLevel;

    private Long requestedById;
    private String requesterName;
    private Department department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
