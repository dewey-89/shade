package com.sparta.miniproject.domain.user.dto;

import lombok.Getter;

@Getter
public class EmailVerificationRequestDto {
    private String email;
    private String authCode;

}
