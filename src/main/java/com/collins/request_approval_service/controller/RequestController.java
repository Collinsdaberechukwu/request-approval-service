package com.collins.request_approval_service.controller;

import com.collins.request_approval_service.dto.Request.CreateRequestDto;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.dto.Update.UpdateRequestDto;
import com.collins.request_approval_service.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@Tag(
        name = "Request Management",
        description = "Endpoints for creating and managing employee requests."
)
public class RequestController {


    private final RequestService requestService;

    @Operation(
            summary = "Create a request",
            description = "Creates a new request for an eligible employee."
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<RequestResponseDto>> createRequest(@Valid @RequestBody CreateRequestDto createRequest){
        return requestService.createRequest(createRequest);
    }

    @Operation(
            summary = "Get request by ID",
            description = "Returns the details of a request."
    )
    @GetMapping("/getRequest/{requestId}")
    public ResponseEntity<ResponseDto<RequestResponseDto>> getRequest(@PathVariable Long requestId){
        return requestService.getRequest(requestId);
    }

    @Operation(
            summary = "Update a request",
            description = "Updates a request that is still pending."
    )
    @PutMapping("/update/{requestId}")
    public ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(@PathVariable Long requestId,
                                                                         @Valid @RequestBody UpdateRequestDto updateRequest){
        return requestService.updateRequest(requestId, updateRequest);
    }

    @Operation(
            summary = "Delete a request",
            description = "Deletes a request that is still pending."
    )
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<ResponseDto<String>> deleteRequest(Long requestId){
        return requestService.deleteRequest(requestId);
    }

    @Operation(
            summary = "Get pending requests",
            description = "Returns a paginated list of pending requests."
    )
    @GetMapping("/pending")
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getPendingRequests(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        return requestService.getPendingRequests(page, size);
    }
}
