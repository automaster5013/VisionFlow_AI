## 🛡️ VisionFlow AI - 지능형 공공시설 및 작업장 안전 관제 시스템

VisionFlow AI는 작업 현장의 CCTV 및 영상 데이터를 실시간으로 분석하여, 작업자의 안전 장비(안전모, 안전조끼 등) 미착용 등 위험 상황을 즉각적으로 감지하고 관리자에게 알리는 End-to-End 지능형 관제 플랫폼입니다.

### ✨ 핵심 기능 (Key Features)

🧠 실시간 AI 객체 탐지 (Custom YOLOv11): - 자체 학습된 모델을 통해 작업자의 안전모(Helmet) 및 안전조끼(Vest) 착용 여부를 실시간으로 판별합니다.

🖥️ 직관적인 통합 관제 대시보드 (React): - AI가 분석 중인 실시간 웹캠/영상 피드를 브라우저에서 확인 가능하며, 바운딩 박스와 신뢰도(Confidence)가 오버레이됩니다.

🚨 실시간 알림 및 통계 시각화: - 위험 상황 감지 시 대시보드 화면 점멸 및 청각적 사이렌 알람이 발생합니다.

위반 유형별(안전모 미착용, 조끼 미착용 등) 누적 통계를 동적 Bar 차트로 한눈에 파악할 수 있습니다.

🐳 완전한 컨테이너화 (Docker Compose): - 프론트엔드, 백엔드, AI 서버, 데이터베이스가 완벽하게 분리된 MSA 구조를 가지며, 단 한 줄의 명령어로 전체 시스템 구축이 가능합니다.

### 🏗️ 시스템 아키텍처 및 디렉토리 구조

시스템은 크게 4개의 독립된 Docker 컨테이너로 격리되어 가상 네트워크 안에서 결합됩니다.
```
VisionFlow_AI/
├── 01_AI_Inference_Server/ # ⚡ [Python/FastAPI] 실시간 영상 분석, 모델 추론 및 스트리밍
├── 02_Backend_API/         # ☕ [Java/Spring Boot] 관제 로그 DB 저장 및 REST API 제공
├── 03_Frontend/            # 📱 [React/Vite] 메인 관제 화면 및 실시간 통계 대시보드
├── 04_MySQL_db/            # 🐬 [MySQL 8.0] 데이터베이스 (Docker 볼륨 매핑)
└── docker-compose.yml      # 🐳 [Infra] 전체 시스템 오케스트레이션 및 네트워크 설정
```

### 🚀 시작하기 (Quick Start)

Docker Desktop이 설치되어 있다면, 복잡한 환경 설정 없이 단 한 줄의 명령어로 전체 관제 시스템을 가동할 수 있습니다.

1. 프로젝트 클론 및 이동

git clone [https://github.com/automaster5013/VisionFlow_AI.git](https://github.com/automaster5013/VisionFlow_AI.git)
cd VisionFlow_AI


2. 전체 시스템 빌드 및 실행

최상위 디렉토리에서 아래 명령어를 실행합니다.

docker-compose up --build


3. 관제 시스템 접속

관제 대시보드 (React): http://localhost:5173

AI 서버 API (FastAPI Docs): http://localhost:8000/docs

백엔드 API (Spring Boot): http://localhost:8080/api/logs

### 🎯 개발 로드맵 (Roadmap)

[x] Phase 1: 시스템 초기 아키텍처 설계 및 데이터베이스 구축

[x] Phase 2: Custom YOLO 모델 학습 및 FastAPI 추론 서버 연동

[x] Phase 3: React 기반 실시간 관제 대시보드 및 통계 차트 구현

[x] Phase 4: Docker Compose를 활용한 전사적 시스템 패키징 (현재 완료)

[ ] Phase 5: Telegram / KakaoTalk 메신저 기반 관리자 실시간 긴급 알림 봇 연동 (Next Step)

[ ] Phase 6: Human Pose Estimation 기반 동적 위험 행동(쓰러짐 등) 감지 기능 추가

© 2026 Team PyvaOps. All rights reserved.