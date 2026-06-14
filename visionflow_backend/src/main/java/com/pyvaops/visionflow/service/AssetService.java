package com.pyvaops.visionflow.service;

import com.pyvaops.visionflow.client.AiServiceClient;
import com.pyvaops.visionflow.dto.AIResponseDto;
import com.pyvaops.visionflow.dto.PredictionResultDto;
import com.pyvaops.visionflow.entity.Asset;
import com.pyvaops.visionflow.entity.AssetHistory;
import com.pyvaops.visionflow.entity.Category;
import com.pyvaops.visionflow.exception.DuplicateAssetCodeException;
import com.pyvaops.visionflow.repository.AssetHistoryRepository;
import com.pyvaops.visionflow.repository.AssetRepository;
import com.pyvaops.visionflow.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.pyvaops.visionflow.dto.AssetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.pyvaops.visionflow.dto.AssetHistoryResponseDto;
import com.pyvaops.visionflow.dto.AssetStatusUpdateRequestDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AiServiceClient aiServiceClient;
    private final CategoryRepository categoryRepository;
    private final AssetRepository assetRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    // 순수 자바 생성자 주입
    public AssetService(AiServiceClient aiServiceClient,
                        CategoryRepository categoryRepository,
                        AssetRepository assetRepository,
                        AssetHistoryRepository assetHistoryRepository) {
        this.aiServiceClient = aiServiceClient;
        this.categoryRepository = categoryRepository;
        this.assetRepository = assetRepository;
        this.assetHistoryRepository = assetHistoryRepository;

        // 기존 포맷: 20260614200039 (년월일시분초가 붙어있음)
        // 🌟 실무 추천 포맷: 20260614-200039 또는 초까지 완벽히 분리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
    }

    // 3. Service 로직 구현
    @Transactional(readOnly = true)
    public Page<AssetResponseDto> getAllAssets(Pageable pageable) {
        return assetRepository.findAll(pageable)
                .map(AssetResponseDto::from);
    }

    public List<AssetHistoryResponseDto> getAssetHistories(Long assetId) {
        // 1. 해당 자산이 실존하는지 먼저 가볍게 체크
        if (!assetRepository.existsById(assetId)) {
            throw new IllegalArgumentException("존재하지 않는 자산 번호입니다: " + assetId);
        }

        // 2. 이력 조회 후 DTO 변환
        return assetHistoryRepository.findByAssetIdOrderByIdDesc(assetId)
                .stream()
                .map(AssetHistoryResponseDto::from)
                .collect(Collectors.toList());
    }

    // 2) Service 로직에 비즈니스 트랜잭션 추가
    @Transactional
    public AssetResponseDto updateAssetStatus(Long assetId, AssetStatusUpdateRequestDto requestDto) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자산을 찾을 수 없습니다. ID: " + assetId));

        String oldStatus = asset.status();
        String newStatus = requestDto.targetStatus().toUpperCase();

        // 🌟 레포지토리 직접 업데이트 가동 (빌더 컴파일 에러 완전 소멸!)
        assetRepository.updateAssetStatusQuery(assetId, newStatus);

        // 이력 저장
        AssetHistory history = AssetHistory.builder()
                .asset(asset)
                .actionType("STATUS_CHANGED")
                .remarks("자산 상태 변경: [" + oldStatus + "] ➡️ [" + newStatus + "] | 사유: " + requestDto.remarks())
                .memberId(requestDto.memberId())
                .build();
        assetHistoryRepository.save(history);

        // 변경된 결과 조회를 위한 객체 리턴
        return new AssetResponseDto(asset.id(), asset.assetCode(), asset.assetName(),
                asset.category() != null ? asset.category().id() : null,
                asset.category() != null ? asset.category().categoryName() : null,
                newStatus, asset.createdAt());
    }

    public void registerAssetViaAi(MultipartFile file, Long memberId) {
        // 1. AI 서버 통신 및 결과 도출
        AIResponseDto aiResponse = aiServiceClient.requestImageInference(file, memberId);

        // 🌟 [디버깅 포인트] AI 서버가 진짜로 보내준 날것의 데이터를 콘솔에 찍어봅니다.
        System.out.println("🔍 [AI Response Raw Data] " + aiResponse);

        // 조건 검사 전 방어 로직 추가
        if (aiResponse == null) {
            System.out.println("⚠️ [경고] AI 응답 객체가 null입니다.");
            return;
        }

        System.out.println("🔍 [체크] Status: " + aiResponse.status() + ", Count: " + aiResponse.detectedCount());

        // 2. 검출된 객체들을 순회하며 MySQL DB 적재 프로세스 가동
        if ("success".equals(aiResponse.status()) && aiResponse.predictions() != null) {

            for (PredictionResultDto prediction : aiResponse.predictions()) {
                String suggestedCode = prediction.aiSuggestedCode();

                // 3. Service 로직에 중복 검증 방어 코드 주입
                // 🌟 [방어 코드 추가] DB에 이미 이 자산 코드가 선점되어 있는지 검사합니다.
                if (assetRepository.existsByAssetCode(suggestedCode)) {
                    throw new DuplicateAssetCodeException("이미 시스템에 등록된 자산 코드입니다: " + suggestedCode);
                }

                String categoryName = prediction.detectedCategory();

                Category category = categoryRepository.findByCategoryName(categoryName)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "데이터베이스에 등록되지 않은 AI 카테고리 자산입니다: " + categoryName));

                // 🌟 자산 엔티티 빌더에 .assetName(categoryName)을 확실하게 적재해 줍니다!
                Asset asset = Asset.builder()
                        .assetCode(suggestedCode)
                        .assetName(categoryName) // 👈 필수 컬럼 데이터 바인딩!
                        .category(category)
                        .status("AVAILABLE")
                        .build();
                Asset savedAsset = assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                        .asset(savedAsset)
                        .actionType("REGISTER_BY_AI")
                        .remarks("YOLOv26 인식을 통한 자산 자동 등록 (신뢰도: " + prediction.confidence() + ")")
                        .memberId(memberId)
                        .build();
                assetHistoryRepository.save(history);

                System.out.println("💾 [VisionFlow DB] 영속화 완료 -> 자산 ID: " + savedAsset.id() + " / 코드: " + suggestedCode);
            }
        } else {
            System.out.println("⚠️ [조건 불일치] AI 분석 결과가 success가 아니거나 예측 리스트가 비어있습니다.");
        }
    }
}