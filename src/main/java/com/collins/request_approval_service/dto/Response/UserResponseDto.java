package com.collins.request_approval_service.dto.Response;

import com.collins.request_approval_service.enums.Department;
import com.collins.request_approval_service.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private Role role;
    private Department department;
    private boolean active;
    private LocalDateTime createdAt;
    private String createdBy;
}
