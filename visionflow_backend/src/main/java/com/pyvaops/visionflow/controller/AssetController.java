package com.pyvaops.visionflow.controller;

import com.pyvaops.visionflow.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pyvaops.visionflow.dto.AssetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import com.pyvaops.visionflow.dto.AssetHistoryResponseDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import com.pyvaops.visionflow.dto.AssetStatusUpdateRequestDto;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/assets")
@CrossOrigin(origins = "*") // 🌟 우선 개발 환경에서의 모든 프론트엔드 요청을 허용합니다!
public class AssetController {
    // 기존 메서드들 (upload, getAllAssets, getAssetHistories 등)
    private final AssetService assetService;

    // 순수 자바 생성자 주입
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // 4. Controller 엔드포인트 개설
    @GetMapping
    public ResponseEntity<Page<AssetResponseDto>> getAllAssets(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AssetResponseDto> assets = assetService.getAllAssets(pageable);
        return ResponseEntity.ok(assets);
    }

    // AssetController 클래스 내부에 추가
    @GetMapping("/{assetId}/histories")
    public ResponseEntity<List<AssetHistoryResponseDto>> getAssetHistories(@PathVariable Long assetId) {
        List<AssetHistoryResponseDto> histories = assetService.getAssetHistories(assetId);
        return ResponseEntity.ok(histories);
    }

    // 3) Controller 엔드포인트 개설
    @PatchMapping("/{assetId}/status")
    public ResponseEntity<AssetResponseDto> updateAssetStatus(
            @PathVariable Long assetId,
            @RequestBody AssetStatusUpdateRequestDto requestDto) {

        AssetResponseDto updatedAsset = assetService.updateAssetStatus(assetId, requestDto);
        return ResponseEntity.ok(updatedAsset);
    }

    /**
     * 포스트맨이나 프론트엔드로부터 이미지를 받아 AI 분석 및 자산 등록을 트리거합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAndAnalyzeAsset(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "memberId", required = false) Long memberId
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("업로드된 이미지 파일이 비어 있습니다.");
        }

        try {
            // 우리가 만든 서비스 레이어 호출 -> 내부적으로 FastAPI 통신 가동
            assetService.registerAssetViaAi(file, memberId);

            return ResponseEntity.ok("이미지가 성공적으로 접수되어 AI 분석 및 자산 등록 프로세스가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("통합 연동 테스트 중 에러 발생: " + e.getMessage());
        }
    }
}