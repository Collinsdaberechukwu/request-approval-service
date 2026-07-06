package com.collins.request_approval_service.service.impl;

import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.UserMapper;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(Long userId){
        log.info("Fetch user by ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found : " + userId));

        UserResponseDto userResponseDto = userMapper.mapToUserResponse(user);
        ResponseDto<UserResponseDto> responseDto = ResponseDto.<UserResponseDto>builder()
                .data(userResponseDto)
                .statusCode(HttpStatus.OK.value())
                .statusMessage("User retrieved successfully")
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponseDto> userResponseDtoList = users.getContent().stream()
                .map(userMapper::mapToUserResponse)
                .toList();

        ResponseDto<List<UserResponseDto>> listResponseDto = new ResponseDto<>(
                HttpStatus.OK.value(),
                "Users retrieved successfully",userResponseDtoList
        );

        return ResponseEntity.ok(listResponseDto);
    }
}
