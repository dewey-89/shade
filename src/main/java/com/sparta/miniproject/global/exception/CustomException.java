package com.sparta.miniproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrrorCode errorCode;

}
