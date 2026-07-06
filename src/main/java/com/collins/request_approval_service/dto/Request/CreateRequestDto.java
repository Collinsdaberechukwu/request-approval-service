package com.collins.request_approval_service.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 150)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10,max = 5000)
    private String description;

    @NotNull(message = "Requester is required")
    @Positive(message = "Requester Id must be greater than zero")
    private Long requestedBy;
}
