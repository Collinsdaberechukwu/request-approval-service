package com.collins.request_approval_service.controller;

import com.collins.request_approval_service.dto.Response.UserResponseDto;
import com.collins.request_approval_service.dto.ResponseDto;
import com.collins.request_approval_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        return userService.getAllUsers(page, size);
    }

}
