package com.collins.request_approval_service.controller;

import com.collins.request_approval_service.dto.Request.CreateRequestDto;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.dto.Update.UpdateRequestDto;
import com.collins.request_approval_service.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {


    private final RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<RequestResponseDto>> createRequest(@Valid @RequestBody CreateRequestDto createRequest){
        return requestService.createRequest(createRequest);
    }

    @GetMapping("/getRequest/{requestId}")
    public ResponseEntity<ResponseDto<RequestResponseDto>> getRequest(@PathVariable Long requestId){
        return requestService.getRequest(requestId);
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(@PathVariable Long requestId,
                                                                         @Valid @RequestBody UpdateRequestDto updateRequest){
        return requestService.updateRequest(requestId, updateRequest);
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<ResponseDto<String>> deleteRequest(Long requestId){
        return requestService.deleteRequest(requestId);
    }

    @GetMapping("/pending")
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getPendingRequests(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        return requestService.getPendingRequests(page, size);
    }
}
