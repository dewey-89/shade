package com.sparta.miniproject.domain.user.controller;

import com.sparta.miniproject.domain.user.dto.SignupRequestDto;
import com.sparta.miniproject.domain.user.service.UserService;
import com.sparta.miniproject.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(hidden = true)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        return userService.signup(signupRequestDto,bindingResult);
    }

}
