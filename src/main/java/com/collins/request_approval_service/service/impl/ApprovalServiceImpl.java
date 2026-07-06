package com.collins.request_approval_service.service.impl;

import com.collins.request_approval_service.dto.Request.ApprovalRequest;
import com.collins.request_approval_service.dto.Response.ApprovalResponse;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.enums.ApprovalDecision;
import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.enums.RequestStatus;
import com.collins.request_approval_service.enums.Role;
import com.collins.request_approval_service.exception.BadRequestsException;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.ApprovalMapper;
import com.collins.request_approval_service.mapper.RequestMapper;
import com.collins.request_approval_service.model.Approval;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.ApprovalRepository;
import com.collins.request_approval_service.repository.RequestRepository;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.ApprovalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ApprovalMapper approvalMapper;

    @Transactional
    @Override
    public ResponseEntity<ResponseDto<RequestResponseDto>> approveRequest(Long requestId,ApprovalRequest approvalRequest) {

        log.info("Approving request={}, approver={}", requestId, approvalRequest.getApproverId());

        Request request = findRequestForUpdate(requestId);
        User approver = findApprover(approvalRequest.getApproverId());
        validateApproval(request, approver);

        Approval approval = Approval.builder()
                .request(request)
                .approvedBy(approver)
                .approvalLevel(request.getCurrentApprovalLevel())
                .decision(ApprovalDecision.APPROVED)
                .comment(approvalRequest.getComment())
                .approvedAt(LocalDateTime.now())
                .build();

        approvalRepository.save(approval);

        moveWorkflow(request);

        requestRepository.save(request);

        RequestResponseDto response = requestMapper.mapToResponse(request);

        return ResponseEntity.ok(
                ResponseDto.<RequestResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("Request approved successfully")
                        .data(response)
                        .build()
        );
    }

    private User findApprover(Long userId){

        return userRepository.findByIdForUpdate(userId).orElseThrow(() -> new ResourceNotFoundException(
                                "Approver not found : " + userId));
    }

    private Request findRequestForUpdate(Long requestId) {
        return requestRepository.findByIdForUpdate(requestId).orElseThrow(() -> new ResourceNotFoundException(
                                "Request not found: " + requestId));
    }

    private void validateApproval(Request request, User approver){
        if(!approver.isActive()){
            throw new BadRequestsException("Inactive user cannot approve requests.");
        }

        if(request.getRequestStatus() != RequestStatus.PENDING){
            throw new BadRequestsException("This request has already been processed.");
        }

        if(approvalRepository.existsByRequestAndApprovalLevel(request, request.getCurrentApprovalLevel())){
            throw new BadRequestsException("This approval level has already been processed.");
        }

        if(!canApprove(request, approver)){
            throw new BadRequestsException("You are not authorized to approve this request.");
        }
    }

    private boolean canApprove(Request request, User approver) {

        return switch (request.getCurrentApprovalLevel()) {

            case MANAGER ->
                    approver.getRole() == Role.MANAGER;

            case DEPARTMENT_HEAD ->
                    approver.getRole() == Role.DEPARTMENT_HEAD;

            case FINANCE ->
                    approver.getRole() == Role.FINANCE;

            case HR ->
                    approver.getRole() == Role.HR;

            case COMPLETED ->
                    throw new BadRequestsException("This request has already completed the approval workflow.");
        };
    }

    private void moveWorkflow(Request request){

        switch (request.getCurrentApprovalLevel()) {

            case MANAGER ->
                    request.setCurrentApprovalLevel(ApprovalLevel.DEPARTMENT_HEAD);

            case DEPARTMENT_HEAD ->
                    request.setCurrentApprovalLevel(ApprovalLevel.FINANCE);

            case FINANCE ->
                    request.setCurrentApprovalLevel(ApprovalLevel.HR);

            case HR -> {
                request.setCurrentApprovalLevel(null);
                request.setRequestStatus(RequestStatus.APPROVED);
            }
        }
    }


    @Override
    public ResponseEntity<ResponseDto<ApprovalResponse>> getApproval(Long approvalId) {

        log.info("Fetching approval by ID: {}", approvalId);

        Approval approval = approvalRepository.findById(approvalId).orElseThrow(() -> new ResourceNotFoundException(
                                "Approval not found: " + approvalId));

        ApprovalResponse approvalResponseDto = approvalMapper.mapToResponse(approval);
        ResponseDto<ApprovalResponse> responseDto = ResponseDto.<ApprovalResponse>builder()
                        .data(approvalResponseDto)
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("Approval retrieved successfully")
                        .build();

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<ResponseDto<List<ApprovalResponse>>> getAllApprovals(int page, int size) {

        log.info("Fetching approvals. page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Approval> approvals = approvalRepository.findAll(pageable);

        List<ApprovalResponse> approvalResponseDto = approvals.getContent()
                .stream()
                .map(approvalMapper::mapToResponse)
                .toList();

        ResponseDto<List<ApprovalResponse>> responseDto = ResponseDto.<List<ApprovalResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("Approvals retrieved successfully")
                        .data(approvalResponseDto)
                        .build();

        return ResponseEntity.ok(responseDto);
    }



}
