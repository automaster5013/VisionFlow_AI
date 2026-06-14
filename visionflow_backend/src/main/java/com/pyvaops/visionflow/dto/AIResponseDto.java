package com.pyvaops.visionflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIResponseDto(
        String status,
        @JsonProperty("detected_count") int detectedCount, // 🌟 파이썬 detected_count와 매핑!
        List<PredictionResultDto> predictions
) {}