package com.collins.request_approval_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private Integer statusCode;
    private String statusMessage;
    private T data;
}
