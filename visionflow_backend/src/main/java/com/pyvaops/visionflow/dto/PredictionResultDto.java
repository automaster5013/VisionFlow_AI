package com.pyvaops.visionflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // 🌟 이 임포트가 필요합니다.

public record PredictionResultDto(
        @JsonProperty("detected_category") String detectedCategory, // 🌟 파이썬 snake_case 매핑!
        Double confidence,
        BoundingBoxDto boundingBox,
        @JsonProperty("ai_suggested_code") String aiSuggestedCode    // 🌟 파이썬 snake_case 매핑!
) {}