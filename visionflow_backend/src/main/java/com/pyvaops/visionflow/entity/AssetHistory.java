package com.pyvaops.visionflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_history")
public class AssetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id") // 🌟 Workbench에 정의된 'history_id'와 일치하는지 확인!
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AssetHistory() {}

    // 빌더 패턴
    public static class Builder {
        private Asset asset;
        private String actionType;
        private String remarks;
        private Long memberId;

        public Builder asset(Asset asset) { this.asset = asset; return this; }
        public Builder actionType(String actionType) { this.actionType = actionType; return this; }
        public Builder remarks(String remarks) { this.remarks = remarks; return this; }
        public Builder memberId(Long memberId) { this.memberId = memberId; return this; }

        public AssetHistory build() {
            AssetHistory history = new AssetHistory();
            history.asset = this.asset;
            history.actionType = this.actionType;
            history.remarks = this.remarks;
            history.memberId = this.memberId;
            return history;
        }
    }

    public static Builder builder() { return new Builder(); }

    public Long id() { return id; }
    public Asset asset() { return asset; }
    public String actionType() { return actionType; }
    public String remarks() { return remarks; }
    public Long memberId() { return memberId; }

    // 🌟 이력 생성 시간을 안전하게 반환할 수 있도록 getter를 추가해 줍니다!
    public java.time.LocalDateTime createdAt() { return createdAt; }
}