package com.sparta.miniproject.domain.user.dto;

import lombok.Getter;

@Getter
public class EmailVerificationResponseDto {
    private boolean verified;

    public EmailVerificationResponseDto(boolean verified) {
        this.verified = verified;
    }
}

