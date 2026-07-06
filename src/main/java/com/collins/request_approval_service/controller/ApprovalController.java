package com.collins.request_approval_service.controller;

import com.collins.request_approval_service.dto.Request.ApprovalRequest;
import com.collins.request_approval_service.dto.Response.ApprovalResponse;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
@Tag(
        name = "Approval Management",
        description = "Endpoints for approving requests and retrieving approval records."
)
public class ApprovalController {

    private final ApprovalService approvalService;

    @Operation(
            summary = "Approve a request",
            description = "Approves a request at its current approval stage."
    )
    @PostMapping("/request/{requestId}/approve")
    public ResponseEntity<ResponseDto<RequestResponseDto>> approveRequest(@PathVariable Long requestId,
                                                                          @Valid @RequestBody ApprovalRequest approvalRequest){
        return approvalService.approveRequest(requestId, approvalRequest);
    }

    @Operation(
            summary = "Get approval by ID",
            description = "Returns the details of a specific approval record."
    )
    @GetMapping("/{approvalId}")
    public ResponseEntity<ResponseDto<ApprovalResponse>> getApproval(@PathVariable Long approvalId){
        return approvalService.getApproval(approvalId);
    }

    @Operation(
            summary = "Get all approvals",
            description = "Returns a paginated list of approval records."
    )
    @GetMapping("/getAll")
    public ResponseEntity<ResponseDto<List<ApprovalResponse>>> getAllApprovals(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return approvalService.getAllApprovals(page, size);
    }

}
