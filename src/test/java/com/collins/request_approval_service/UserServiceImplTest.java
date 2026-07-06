package com.collins.request_approval_service;
import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.enums.Department;
import com.collins.request_approval_service.enums.Role;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.UserMapper;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .firstName("Collins")
                .lastName("Okafor")
                .email("collins@test.com")
                .role(Role.EMPLOYEE)
                .department(Department.OPERATIONS)
                .active(true)
                .build();

        user.setId(1L);

        UserResponseDto.builder()
                .id(1L)
                .firstName("Collins")
                .lastName("Okafor")
                .email("collins@test.com")
                .role(Role.EMPLOYEE)
                .department(Department.OPERATIONS)
                .active(true)
                .build();
    }

    @Test
    void shouldGetUserSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToUserResponse(user))
                .thenReturn(responseDto);

        ResponseEntity<ResponseDto<UserResponseDto>> response =
                userService.getUser(1L);

        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(
                "User retrieved successfully",
                response.getBody().getStatusMessage());

        assertEquals(
                responseDto,
                response.getBody().getData());

        verify(userRepository).findById(1L);
        verify(userMapper).mapToUserResponse(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> userService.getUser(1L));

        assertEquals(
                "User not found : 1",
                exception.getMessage());

        verify(userRepository).findById(1L);
        verify(userMapper, never()).mapToUserResponse(any());
    }

    @Test
    void shouldGetAllUsersSuccessfully() {

        Page<User> page =
                new PageImpl<>(List.of(user));

        when(userRepository.findAll(PageRequest.of(0,10)))
                .thenReturn(page);

        when(userMapper.mapToUserResponse(user))
                .thenReturn(responseDto);

        ResponseEntity<ResponseDto<List<UserResponseDto>>> response =
                userService.getAllUsers(0,10);

        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(
                "Users retrieved successfully",
                response.getBody().getStatusMessage());

        assertEquals(
                1,
                response.getBody().getData().size());

        verify(userRepository).findAll(PageRequest.of(0,10));
        verify(userMapper).mapToUserResponse(user);
    }

    @Test
    void shouldReturnEmptyUserList() {

        Page<User> page =
                new PageImpl<>(List.of());

        when(userRepository.findAll(PageRequest.of(0,10)))
                .thenReturn(page);

        ResponseEntity<ResponseDto<List<UserResponseDto>>> response =
                userService.getAllUsers(0,10);

        assertNotNull(response);

        assertTrue(response.getBody().getData().isEmpty());

        assertEquals(
                "Users retrieved successfully",
                response.getBody().getStatusMessage());

        verify(userRepository).findAll(PageRequest.of(0,10));
        verify(userMapper, never()).mapToUserResponse(any());
    }
}
