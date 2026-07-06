package com.collins.request_approval_service.controller;

import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(
        name = "User Management",
        description = "Endpoints for retrieving user information."
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get user by ID",
            description = "Returns the details of a user."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @Operation(
            summary = "Get all users",
            description = "Returns a paginated list of users."
    )
    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        return userService.getAllUsers(page, size);
    }

}
