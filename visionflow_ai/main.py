import io
from datetime import datetime
from fastapi import FastAPI, UploadFile, File, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import PIL.Image as Image

# YOLOv26 패키지 로드 (Ultralytics 엔진 기반)
from ultralytics import YOLO

app = FastAPI(
    title="VisionFlow AI Service API",
    description="이미지 인식 기반 스마트 재고/자산 관리 시스템 - AI 추론 모듈",
    version="1.0.0"
)

# 1. CORS 미들웨어 설정 (Spring Boot 메인 서버와의 통신 허용)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 추후 운영 환경에서는 Spring Boot 서버의 IP/도메인만 지정하는 것을 권장.
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 2. YOLOv26 모델 로드
# 사전에 학습된 가중치 파일(.pt) 경로를 지정. 파일이 없다면 실행 시 ultralytics가 자동으로 기본 가중치를 다운로드.
try:
    model = YOLO("yolov8n.pt")  # 프로젝트 요구사항 및 가상환경 검증 단계에 맞춰 'yolov26n.pt' 등으로 변경 가능
except Exception as e:
    print(f"⚠️ 모델 로드 중 오류 발생: {e}")
    model = None

# 응답 데이터 정합성을 위한 Pydantic 스키마 정의 (선택사항이나 API 명세서 자동화를 위해 권장)
class BoundingBox(BaseModel):
    xmin: int
    ymin: int
    xmax: int
    ymax: int

class PredictionResult(BaseModel):
    detected_category: str
    confidence: float
    bounding_box: BoundingBox
    ai_suggested_code: str

class AIResponse(BaseModel):
    status: str
    detected_count: int
    predictions: list[PredictionResult]


@app.get("/")
def health_check():
    return {
        "status": "healthy",
        "message": "VisionFlow AI 가상환경 및 FastAPI 서버가 정상적으로 구동 중입니다."
    }


@app.post("/api/v1/ai/predict", response_model=AIResponse, status_code=status.HTTP_200_OK)
async def predict_asset(
    image: UploadFile = File(..., description="YOLOv26 모델이 분석할 자산 촬영 이미지 파일"),
    member_id: int = None
):
    """
    Spring Boot 서버로부터 이미지를 받아 YOLOv26 객체 인식을 수행하고,
    DB 적재에 최적화된 형태의 자산 카테고리 및 제안 코드를 반환.
    """
    # 1. 파일 확장자 검증
    if not image.filename.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="지원하지 않는 이미지 형식입니다. png, jpg, jpeg, webp 파일만 업로드 가능합니다."
        )

    if not model:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail="AI 모델이 준비되지 않았습니다. 서버 로그를 확인하세요."
        )

    try:
        # 2. 업로드된 파일 바이트를 읽어와 PIL 이미지 객체로 변환 (서버 디스크 I/O 낭비 방지)
        image_bytes = await image.read()
        pil_image = Image.open(io.BytesIO(image_bytes)).convert("RGB")

        # 3. YOLOv26 모델 추론 실행
        results = model(pil_image)
        result = results[0]  # 단건 이미지 추론이므로 첫 번째 결과 선택

        predictions = []
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

        # 4. 추론 결과 파싱 및 Spring Boot 맞춤형 DTO 매핑
        for i, box in enumerate(result.boxes):
            # 바운딩 박스 좌표 추출 (정수형 변환)
            coords = box.xyxy[0].tolist()
            xmin, ymin, xmax, ymax = map(int, coords)

            # 검출된 클래스 인덱스 및 클래스명 매핑
            class_id = int(box.cls[0].item())
            class_name = result.names[class_id].upper()  # DB category_name과 매핑을 위해 대문자 변환
            
            # 신뢰도 (Confidence Score)
            confidence = float(box.conf[0].item())

            # 🌟 [설계 핵심] Spring Boot가 즉시 사용할 수 있는 고유 자산 코드 포맷 제안
            # 포맷 예시: AST-LAPTOP-20260613153022-0
            ai_suggested_code = f"AST-{class_name}-{timestamp}-{i}"

            predictions.append(
                PredictionResult(
                    detected_category=class_name,
                    confidence=round(confidence, 3),
                    bounding_box=BoundingBox(xmin=xmin, ymin=ymin, xmax=xmax, ymax=ymax),
                    ai_suggested_code=ai_suggested_code
                )
            )

        return AIResponse(
            status="success",
            detected_count=len(predictions),
            predictions=predictions
        )

    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"AI 추론 과정에서 내부 오류가 발생했습니다: {str(e)}"
        )