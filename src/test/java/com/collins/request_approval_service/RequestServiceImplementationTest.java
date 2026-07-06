package com.collins.request_approval_service;

import com.collins.request_approval_service.dto.Request.CreateRequestDto;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.enums.Department;
import com.collins.request_approval_service.enums.RequestStatus;
import com.collins.request_approval_service.enums.Role;
import com.collins.request_approval_service.exception.RequestAlreadyExistException;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.RequestMapper;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.RequestRepository;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.impl.RequestServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplementationTest {


    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImplementation requestService;

    private User requester;
    private Request request;
    private CreateRequestDto createRequestDto;
    private RequestResponseDto requestResponseDto;


    @BeforeEach
    void setUp() {

        requester = User.builder()
                .firstName("Collins")
                .lastName("Okafor")
                .email("collins@test.com")
                .role(Role.EMPLOYEE)
                .department(Department.OPERATIONS)
                .active(true)
                .build();

        requester.setId(1L);

        createRequestDto = new CreateRequestDto();
        createRequestDto.setTitle("Leave Request");
        createRequestDto.setDescription("Annual leave");
        createRequestDto.setRequestedBy(1L);

        request = Request.builder()
                .title(createRequestDto.getTitle())
                .description(createRequestDto.getDescription())
                .requestedBy(requester)
                .requestStatus(RequestStatus.PENDING)
                .currentApprovalLevel(ApprovalLevel.MANAGER)
                .build();

        request.setId(10L);

        requestResponseDto = new RequestResponseDto();
        requestResponseDto.setRequestedById(10L);
        requestResponseDto.setTitle("Leave Request");
    }

    @Test
    void shouldCreateRequestSuccessfully() {

        when(userRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(requester));

        when(requestRepository.existsByTitleIgnoreCaseAndRequestedByAndRequestStatus(
                anyString(),
                any(User.class),
                eq(RequestStatus.PENDING)))
                .thenReturn(false);

        when(requestMapper.mapToResponse(any(Request.class)))
                .thenReturn(requestResponseDto);

        ResponseEntity<ResponseDto<RequestResponseDto>> response = requestService.createRequest(createRequestDto);

        assertNotNull(response);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(
                "Request created successfully",
                response.getBody().getStatusMessage());

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);

        verify(requestRepository).save(captor.capture());

        Request saved = captor.getValue();

        assertEquals("Leave Request", saved.getTitle());

        assertEquals(RequestStatus.PENDING, saved.getRequestStatus());

        assertEquals(ApprovalLevel.MANAGER,
                saved.getCurrentApprovalLevel());

        assertEquals(requester, saved.getRequestedBy());

        verify(requestMapper).mapToResponse(saved);
    }

    @Test
    void shouldThrowExceptionWhenRequesterDoesNotExist() {

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> requestService.createRequest(createRequestDto));

        verify(requestRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRequesterIsInactive() {

        requester.setActive(false);

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(requester));

        assertThrows(ResourceNotFoundException.class, () -> requestService.createRequest(createRequestDto));
        verify(requestRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRequesterIsNotEmployee() {

        requester.setRole(Role.MANAGER);

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(requester));

        assertThrows(ResourceNotFoundException.class, () -> requestService.createRequest(createRequestDto));
        verify(requestRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicatePendingRequestExists() {

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(requester));

        when(requestRepository
                .existsByTitleIgnoreCaseAndRequestedByAndRequestStatus(
                        anyString(),
                        any(User.class),
                        eq(RequestStatus.PENDING)))
                .thenReturn(true);

        assertThrows(RequestAlreadyExistException.class, () -> requestService.createRequest(createRequestDto));
        verify(requestRepository, never()).save(any());
    }


}
