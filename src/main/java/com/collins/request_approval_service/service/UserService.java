package com.collins.request_approval_service.service;

import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ResponseEntity<ResponseDto<UserResponseDto>> getUser(Long userId);

    ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers(int page, int size);
}
