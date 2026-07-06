package com.collins.request_approval_service;


import com.collins.request_approval_service.dto.Request.ApprovalRequest;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.enums.*;
import com.collins.request_approval_service.exception.BadRequestsException;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.RequestMapper;
import com.collins.request_approval_service.model.Approval;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.ApprovalRepository;
import com.collins.request_approval_service.repository.RequestRepository;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.impl.ApprovalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApprovalServiceImplTest {


    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    private User manager;

    private User employee;

    private Request request;

    private ApprovalRequest approvalRequest;

    private RequestResponseDto responseDto;


    @BeforeEach
    void setUp() {

        employee = User.builder()
                .firstName("Collins")
                .lastName("Okafor")
                .email("employee@test.com")
                .department(Department.OPERATIONS)
                .role(Role.EMPLOYEE)
                .active(true)
                .build();

        employee.setId(1L);

        manager = User.builder()
                .firstName("Daberechi")
                .lastName("Manager")
                .email("manager@test.com")
                .department(Department.OPERATIONS)
                .role(Role.MANAGER)
                .active(true)
                .build();

        manager.setId(2L);

        request = Request.builder()
                .title("Leave Request")
                .description("Annual Leave")
                .requestStatus(RequestStatus.PENDING)
                .currentApprovalLevel(ApprovalLevel.MANAGER)
                .requestedBy(employee)
                .build();

        request.setId(10L);

        approvalRequest = new ApprovalRequest();
        approvalRequest.setApproverId(2L);
        approvalRequest.setComment("Approved");

        responseDto = new RequestResponseDto();
        responseDto.setRequestedById(10L);
        responseDto.setTitle("Leave Request");
    }

    @Test
    void shouldApproveRequestSuccessfully() {

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        when(approvalRepository.existsByRequestAndApprovalLevel(
                request,
                ApprovalLevel.MANAGER))
                .thenReturn(false);

        when(requestMapper.mapToResponse(any(Request.class)))
                .thenReturn(responseDto);

        ResponseEntity<ResponseDto<RequestResponseDto>> response =
                approvalService.approveRequest(10L, approvalRequest);

        assertNotNull(response);

        assertEquals(
                "Request approved successfully",
                response.getBody().getStatusMessage());

        verify(approvalRepository).save(any(Approval.class));

        verify(requestRepository).save(any(Request.class));

        assertEquals(
                ApprovalLevel.DEPARTMENT_HEAD,
                request.getCurrentApprovalLevel());
    }

    @Test
    void shouldThrowExceptionWhenRequestDoesNotExist() {

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenApproverDoesNotExist() {

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenApproverIsInactive() {

        manager.setActive(false);

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        assertThrows(
                BadRequestsException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRequestAlreadyApproved() {

        request.setRequestStatus(RequestStatus.APPROVED);

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        assertThrows(
                BadRequestsException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenApprovalAlreadyExists() {

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        when(approvalRepository.existsByRequestAndApprovalLevel(
                request,
                ApprovalLevel.MANAGER))
                .thenReturn(true);

        assertThrows(
                BadRequestsException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenApproverHasWrongRole() {

        manager.setRole(Role.HR);

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        when(approvalRepository.existsByRequestAndApprovalLevel(
                request,
                ApprovalLevel.MANAGER))
                .thenReturn(false);

        assertThrows(
                BadRequestsException.class,
                () -> approvalService.approveRequest(10L, approvalRequest));

        verify(approvalRepository, never()).save(any());
    }

    @Test
    void shouldPersistApprovalRecord() {

        when(requestRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(request));

        when(userRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(manager));

        when(approvalRepository.existsByRequestAndApprovalLevel(
                request,
                ApprovalLevel.MANAGER))
                .thenReturn(false);

        when(requestMapper.mapToResponse(any()))
                .thenReturn(responseDto);

        approvalService.approveRequest(10L, approvalRequest);

        ArgumentCaptor<Approval> captor =
                ArgumentCaptor.forClass(Approval.class);

        verify(approvalRepository).save(captor.capture());

        Approval saved = captor.getValue();

        assertEquals(manager, saved.getApprovedBy());

        assertEquals(request, saved.getRequest());

        assertEquals(
                ApprovalDecision.APPROVED,
                saved.getDecision());

        assertEquals(
                ApprovalLevel.MANAGER,
                saved.getApprovalLevel());

        assertEquals(
                "Approved",
                saved.getComment());

        assertNotNull(saved.getApprovedAt());
    }
}
