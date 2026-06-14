package com.pyvaops.visionflow.exception;

// 2. 비즈니스 예외(Custom Exception) 정의
public class DuplicateAssetCodeException extends RuntimeException {
    public DuplicateAssetCodeException(String message) {
        super(message);
    }
}