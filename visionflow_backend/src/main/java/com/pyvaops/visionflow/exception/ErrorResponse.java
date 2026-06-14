package com.pyvaops.visionflow.exception;

import java.time.LocalDateTime;

// 1. 표준 에러 응답 형식을 담을 에러 DTO 생성
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}