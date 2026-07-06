package com.collins.request_approval_service.service;

import com.collins.request_approval_service.dto.Request.ApprovalRequest;
import com.collins.request_approval_service.dto.Response.ApprovalResponse;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApprovalService {

    @Transactional
    ResponseEntity<ResponseDto<RequestResponseDto>> approveRequest(Long requestId, ApprovalRequest dto);

    ResponseEntity<ResponseDto<ApprovalResponse>> getApproval(Long approvalId);

    ResponseEntity<ResponseDto<List<ApprovalResponse>>> getAllApprovals(int page, int size);

}
