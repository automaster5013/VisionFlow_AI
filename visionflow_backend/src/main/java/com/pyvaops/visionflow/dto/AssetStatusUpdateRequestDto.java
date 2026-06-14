package com.pyvaops.visionflow.dto;

// 1) 상태 변경용 Request DTO 생성
public record AssetStatusUpdateRequestDto(
    String targetStatus, // 예: USING, UNDER_REPAIR, DISPOSED
    Long memberId,       // 상태를 변경한 임직원 ID
    String remarks       // 변경 사유 (예: "장비 노후화로 인한 폐기")
) {}