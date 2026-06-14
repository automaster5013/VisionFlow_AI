package com.pyvaops.visionflow.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// 4. 전역 예외 처리기(Global Exception Handler) 구현
@RestControllerAdvice // 🌟 모든 컨트롤러의 예외를 중앙 집중식으로 감시합니다.
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAssetCodeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateAssetCode(
            DuplicateAssetCodeException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(), // 409 Conflict
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}