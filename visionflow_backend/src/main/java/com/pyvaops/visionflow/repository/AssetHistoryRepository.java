package com.pyvaops.visionflow.repository;

import com.pyvaops.visionflow.entity.AssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    // 2. Repository에 특정 자산 ID 필터링 쿼리 추가
    // 🌟 특정 자산 ID에 해당하는 이력 목록을 historyId 내림차순(최신순)으로 전체 조회
    List<AssetHistory> findByAssetIdOrderByIdDesc(Long assetId);
}