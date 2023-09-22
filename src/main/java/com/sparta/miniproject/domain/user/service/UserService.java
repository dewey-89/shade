package com.sparta.miniproject.domain.user.service;

import com.sparta.miniproject.domain.user.dto.SignupRequestDto;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.domain.user.repository.EmailVerificationRepository;
import com.sparta.miniproject.domain.user.repository.UserRepository;
import com.sparta.miniproject.global.dto.ApiResponse;
import com.sparta.miniproject.global.exception.CustomException;
import com.sparta.miniproject.global.exception.ErrrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${admin.token}")
    private String ADMIN_TOKEN;
    public ResponseEntity<ApiResponse<String>> signup(SignupRequestDto requestDto, BindingResult bindingResult) {

        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ApiResponse.error(fieldError.getDefaultMessage()));
            }
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrrorCode.DUPLICATE_USERNAME);
        }

        // 이메일 중복 확인
        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrrorCode.DUPLICATE_EMAIL);
        }

        //닉네임 중복 확인
        Optional<UserEntity> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new CustomException(ErrrorCode.DUPLICATE_NICKNAME);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        UserEntity userEntity = new UserEntity(username, password, email, nickname, role);
        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successMessage("회원가입이 완료되었습니다."));

    }
}
