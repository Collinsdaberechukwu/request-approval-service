package com.collins.request_approval_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(
        name = "ResponseDto",
        description = "Standard response wrapper returned by all API endpoints."
)
public class ResponseDto<T> {

    @Schema(
            description = "HTTP status code returned by the API.",
            example = "200"
    )
    private Integer statusCode;

    @Schema(
            description = "A brief message describing the outcome of the request.",
            example = "Request retrieved successfully"
    )
    private String statusMessage;

    @Schema(
            description = "The response payload. The type depends on the endpoint being called."
    )
    private T data;
}
