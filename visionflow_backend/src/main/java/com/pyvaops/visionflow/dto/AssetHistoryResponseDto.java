package com.pyvaops.visionflow.dto;

import com.pyvaops.visionflow.entity.AssetHistory;
import java.time.LocalDateTime;

// 1. 프론트엔드용 이력 응답 DTO 설계
public record AssetHistoryResponseDto(
        Long historyId,
        Long assetId,
        String assetCode,
        String actionType,
        String remarks,
        Long memberId,
        LocalDateTime createdAt
) {
    public static AssetHistoryResponseDto from(AssetHistory history) {
        return new AssetHistoryResponseDto(
                history.id(),
                history.asset() != null ? history.asset().id() : null,
                history.asset() != null ? history.asset().assetCode() : null,
                history.actionType(),
                history.remarks(),
                history.memberId(),
                history.createdAt()
        );
    }
}