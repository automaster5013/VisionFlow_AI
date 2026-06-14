package com.pyvaops.visionflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset")
// ... 상단 생략 ...
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long id;

    @Column(name = "asset_code", nullable = false, unique = true)
    private String assetCode;

    // 🌟 [추가] DB의 asset_name 필수 컬럼과 매핑할 필드를 선언합니다!
    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    public Asset() {}

    // 빌더 패턴에 assetName 추가
    public static class Builder {
        private String assetCode;
        private String assetName; // 🌟 추가
        private Category category;
        private String status = "AVAILABLE";

        public Builder assetCode(String assetCode) { this.assetCode = assetCode; return this; }
        public Builder assetName(String assetName) { this.assetName = assetName; return this; } // 🌟 추가
        public Builder category(Category category) { this.category = category; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public Asset build() {
            Asset asset = new Asset();
            asset.assetCode = this.assetCode;
            asset.assetName = this.assetName; // 🌟 추가
            asset.category = this.category;
            asset.status = this.status;
            return asset;
        }
    }

    public static Builder builder() { return new Builder(); }

    public Long id() { return id; }
    public String assetCode() { return assetCode; }
    public String assetName() { return assetName; } // 🌟 추가
    public Category category() { return category; }
    public String status() { return status; }

    // 🌟 이 메서드가 누락되어 빌드가 터졌습니다! 새로 추가해 줍니다.
    public java.time.LocalDateTime createdAt() { return createdAt; }
}