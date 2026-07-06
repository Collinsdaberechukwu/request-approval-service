package com.collins.request_approval_service.service.impl;

import com.collins.request_approval_service.dto.Request.CreateRequestDto;
import com.collins.request_approval_service.dto.Response.RequestResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.dto.Update.UpdateRequestDto;
import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.enums.RequestStatus;
import com.collins.request_approval_service.enums.Role;
import com.collins.request_approval_service.exception.InvalidApprovalException;
import com.collins.request_approval_service.exception.RequestAlreadyExistException;
import com.collins.request_approval_service.exception.ResourceNotFoundException;
import com.collins.request_approval_service.mapper.ApprovalMapper;
import com.collins.request_approval_service.mapper.RequestMapper;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import com.collins.request_approval_service.repository.ApprovalRepository;
import com.collins.request_approval_service.repository.RequestRepository;
import com.collins.request_approval_service.repository.UserRepository;
import com.collins.request_approval_service.service.RequestService;
import jakarta.transaction.Transactional;
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
@Transactional
public class RequestServiceImplementation implements RequestService {

    private final RequestRepository requestRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ApprovalMapper approvalMapper;


    @Override
    public ResponseEntity<ResponseDto<RequestResponseDto>> createRequest(CreateRequestDto createRequest) {

        log.info("Creating request for user {}", createRequest.getRequestedBy());

        User requester = findUser(createRequest.getRequestedBy());

        validateRequester(requester);

        validateDuplicateRequest(createRequest, requester);

        Request request = Request.builder()
                .title(createRequest.getTitle().trim())
                .description(createRequest.getDescription().trim())
                .requestStatus(RequestStatus.PENDING)
                .currentApprovalLevel(getInitialApprovalLevel())
                .requestedBy(requester)
                .build();

        requestRepository.save(request);

        log.info("Request {} created successfully", request.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.<RequestResponseDto>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .statusMessage("Request created successfully")
                        .data(requestMapper.mapToResponse(request))
                        .build());
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseDto<RequestResponseDto>> getRequest(Long requestId) {

        log.info("Fetching request {}", requestId);

        Request request = findRequest(requestId);

        return ResponseEntity.ok(
                ResponseDto.<RequestResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("Request retrieved successfully")
                        .data(requestMapper.mapToResponse(request))
                        .build()
        );

    }

    @Override
    public ResponseEntity<ResponseDto<RequestResponseDto>> updateRequest(Long requestId, UpdateRequestDto updateRequest) {

        Request request = findRequestForUpdate(requestId);

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new InvalidApprovalException("Only pending requests can be updated.");
        }

        request.setTitle(updateRequest.getTitle().trim());
        request.setDescription(updateRequest.getDescription().trim());

        requestRepository.save(request);

        return ResponseEntity.ok(
                ResponseDto.<RequestResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("Request updated successfully")
                        .data(requestMapper.mapToResponse(request))
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResponseDto<String>> deleteRequest(Long requestId) {

        Request request = findRequestForUpdate(requestId);

        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new InvalidApprovalException("Approved requests cannot be deleted.");
        }

        requestRepository.delete(request);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "Request deleted successfully",
                        null
                )
        );
    }

    private User findUser(Long userId) {
        return userRepository.findByIdForUpdate(userId).orElseThrow(() -> new ResourceNotFoundException("User not found : "
                + userId));

    }

    private Request findRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found : " + requestId));

    }

    private Request findRequestForUpdate(Long requestId) {
        return requestRepository.findByIdForUpdate(requestId).orElseThrow(() -> new ResourceNotFoundException(
                "Request not found : " + requestId));
    }

    private ApprovalLevel getInitialApprovalLevel() {
        return ApprovalLevel.MANAGER;
    }

    private void validateRequester(User requester) {
        if (!requester.isActive()){
            throw new ResourceNotFoundException("Inactive users cannot create requests.");
        }

        if (requester.getRole() != Role.EMPLOYEE) {
            throw new ResourceNotFoundException("Only employees can create requests.");
        }
    }

    private void validateDuplicateRequest(CreateRequestDto createRequestDto, User requester) {

        boolean exists = requestRepository.existsByTitleIgnoreCaseAndRequestedByAndRequestStatus(createRequestDto.getTitle().trim(),
                        requester, RequestStatus.PENDING);

        if (exists) {
            throw new RequestAlreadyExistException("A pending request with this title already exists.");
        }
    }


    @Override
    public ResponseEntity<ResponseDto<List<RequestResponseDto>>> getPendingRequests(int page, int size) {

        log.info("Fetching pending requests. page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Request> requests =
                requestRepository.findByRequestStatus(
                        RequestStatus.PENDING,
                        pageable);

        List<RequestResponseDto> response = requests.getContent()
                .stream()
                .map(requestMapper::mapToResponse)
                .toList();

        ResponseDto<List<RequestResponseDto>> responseDto = ResponseDto.<List<RequestResponseDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .statusMessage("Pending requests retrieved successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}
