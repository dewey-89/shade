package com.sparta.miniproject.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailAuthRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    public String email;
}
