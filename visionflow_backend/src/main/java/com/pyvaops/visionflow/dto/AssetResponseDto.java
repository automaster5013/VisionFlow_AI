package com.pyvaops.visionflow.dto;

import com.pyvaops.visionflow.entity.Asset;
import java.time.LocalDateTime;

// 1. 프론트엔드에 전달할 응답 DTO 설계
public record AssetResponseDto(
        Long assetId,
        String assetCode,
        String assetName,
        Integer categoryId,
        String categoryName,
        String status,
        LocalDateTime createdAt
) {
    // 🌟 엔티티를 DTO로 편하게 변환하기 위한 정적 팩토리 메서드
    public static AssetResponseDto from(Asset asset) {
        return new AssetResponseDto(
                asset.id(),
                asset.assetCode(),
                asset.assetName(),
                asset.category() != null ? asset.category().id() : null,
                asset.category() != null ? asset.category().categoryName() : null,
                asset.status(),
                asset.createdAt()
        );
    }
}