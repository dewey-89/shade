package com.sparta.miniproject.domain.user.service;

import com.sparta.miniproject.domain.user.dto.SignupRequestDto;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.domain.user.repository.EmailVerificationRepository;
import com.sparta.miniproject.domain.user.repository.UserRepository;
import com.sparta.miniproject.global.dto.ApiResponse;
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("중복된 회원이 존재합니다."));
        }

        // 이메일 중복 확인
        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("중복된 이메일이 존재합니다."));
        }

        //닉네임 중복 확인
        Optional<UserEntity> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("중복된 닉네임이 존재합니다."));
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("관리자 토큰이 일치하지 않습니다."));
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        UserEntity userEntity = new UserEntity(username, password, email, nickname, role);
        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("회원가입이 완료되었습니다."));

    }
}
