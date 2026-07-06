package com.collins.request_approval_service.mapper;

import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserResponseDto mapToUserResponse(User user){

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .department(user.getDepartment())
                .active(user.isActive())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
