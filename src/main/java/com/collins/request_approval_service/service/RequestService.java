package com.collins.request_approval_service.service;

import com.collins.request_approval_service.dto.Request.CreateRequestDto;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.dto.Update.UpdateRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {


    ResponseEntity<ResponseDto<RequestResponseDto>> createRequest(CreateRequestDto createRequest);

    @Transactional
    ResponseEntity<ResponseDto<RequestResponseDto>> getRequest(Long id);

    ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(
            Long requestId,
            UpdateRequestDto dto);

    ResponseEntity<ResponseDto<String>> deleteRequest(Long requestId);

    ResponseEntity<ResponseDto<List<RequestResponseDto>>> getPendingRequests(int page, int size);
}
