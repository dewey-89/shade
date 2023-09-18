package com.sparta.miniproject.global.entity;

import lombok.Getter;

@Getter
public class ResponseMessage {
    private String message;
    private int statusCode;

    public ResponseMessage(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

}