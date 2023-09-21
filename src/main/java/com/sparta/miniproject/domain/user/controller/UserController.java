package com.sparta.miniproject.domain.user.controller;

import com.sparta.miniproject.domain.user.dto.EmailAuthRequestDto;
import com.sparta.miniproject.domain.user.dto.SignupRequestDto;
import com.sparta.miniproject.domain.user.service.EmailService;
import com.sparta.miniproject.domain.user.service.UserService;
import com.sparta.miniproject.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @Operation(hidden = true)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        return userService.signup(signupRequestDto,bindingResult);
    }

    @PostMapping("/mailConfirm")
    public String mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {

        String authCode = emailService.sendEmail(emailDto.getEmail());
        return authCode;
    }

}
