package com.collins.request_approval_service.dto.Update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 5,max = 150)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10,max = 5000)
    private String description;
}
