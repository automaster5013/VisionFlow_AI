package com.pyvaops.visionflow.repository;

import com.pyvaops.visionflow.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<Asset> findAll(Pageable pageable);

    // 🌟 [새로 추가] 자산 코드가 이미 DB에 존재하는지 여부를 확인하는 Spring Data JPA 쿼리 메서드
    boolean existsByAssetCode(String assetCode);

    // (아까 2안 우회책으로 추가한 수정 쿼리가 있다면 그대로 유지)
    @Modifying
    @Query("UPDATE Asset a SET a.status = :status WHERE a.id = :id")
    int updateAssetStatusQuery(@Param("id") Long id, @Param("status") String status);
}