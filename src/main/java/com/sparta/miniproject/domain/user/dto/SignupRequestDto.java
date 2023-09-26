package com.sparta.miniproject.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank (message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "아이디는 영문 소문자와 숫자로만 입력해주세요.")
    @Size(min = 4, max = 10, message = "아이디는 4자 이상 10자 이하로 입력해주세요.")
    private String username;

    @NotBlank (message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9_~!@#$%^&*()-+|}{:;'\".,<>?]+$", message = "비밀번호는 영문 대소문자, 숫자, 특수문자로만 입력해주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
    private String password;

    @NotBlank (message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 옳바르지 않습니다.")
    private String email;

    @NotBlank
    private String nickname;

    private boolean admin = false;
    private String adminToken = "";
}
