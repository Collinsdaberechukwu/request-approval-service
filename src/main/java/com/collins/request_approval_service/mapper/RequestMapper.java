package com.collins.request_approval_service.mapper;

import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public RequestResponseDto mapToResponse(Request request){

        User user = request.getRequestedBy();

        return RequestResponseDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .requestStatus(request.getRequestStatus())
                .currentApprovalLevel(request.getCurrentApprovalLevel())
                .requestedById(user.getId())
                .requesterName(user.getFirstName() + " " + user.getLastName())
                .department(user.getDepartment())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .version(request.getVersion())
                .build();

    }
}
