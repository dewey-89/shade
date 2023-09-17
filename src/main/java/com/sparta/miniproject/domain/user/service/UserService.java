package com.sparta.miniproject.domain.user.service;

import com.sparta.miniproject.domain.user.dto.SignupRequestDto;
import com.sparta.miniproject.domain.user.entity.User;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<String> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                return new ResponseEntity<>("상태코드 : " + HttpStatus.BAD_REQUEST.value() + ", 메세지 : " + fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, nickname, role);
        userRepository.save(user);

        return ResponseEntity.status(200).body("msg : 회원가입 성공, statusCode : 200");

    }
}
