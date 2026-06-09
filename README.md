# 📦 VisionFlow : AI 이미지 인식 기반 스마트 자산 관리 플랫폼 (1/2)

> **"Spring Boot의 견고한 백엔드와 Python의 고성능 AI 추론 엔진을 DevOps 파이프라인으로 관통하다."**
> 
> **PyvaOps (파이바옵스) 팀**의 1인 올라운더 프로젝트로, 사내 자산 및 물류 재고를 AI 이미지 인식을 통해 자동화하고 통합 관리하는 고성능 비즈니스 솔루션입니다.

---

## 🚀 1. Project Architecture

VisionFlow는 자바 진영의 안정적인 트랜잭션 처리와 파이썬 진영의 혁신적인 AI 인프라를 분리 및 결합한 **하이브리드 마이크로서비스 스타일(MSA-like) 아키텍처**를 지향합니다.

[ Client (Web/Web-App) ]
│ (HTTP/JSON)
▼
[ Spring Boot Backend Server ] (Port: 8080)
│
├─ (JPA) ──▶ [ MySQL Database ] (Port: 3306)
│
└─ (WebClient) ──▶ [ FastAPI AI Inference Server ] (Port: 8000)
└─ (Inference) ──▶ [ YOLOv11 Engine ]


* **역할의 명확한 분리:** Spring Boot는 비즈니스 로직, 회원 인가(JWT), DB 트랜잭션 제어에 집중하고, FastAPI는 경량화된 AI 모델 추론 및 이미지 전처리 인터페이스 역할만 전담하여 시스템 결합도를 낮췄습니다.
* **DevOps 빌드업:** 각 컴포넌트는 독립된 컨테이너로 패키징되어 데브옵스 파이프라인을 통해 유기적으로 배포 및 모니터링됩니다.

---

## 🛠️ 2. Tech Stacks

### Backend & Web
* **Language/Framework:** Java 17, Spring Boot 3.x
* **Data Access:** Spring Data JPA
* **Security:** Spring Security, JWT (Json Web Token)
* **Network Communication:** Spring WebClient (Async/Non-blocking HTTP Client)

### Artificial Intelligence
* **Language/Framework:** Python 3.11, FastAPI
* **Computer Vision Engine:** YOLOv11 (You Only Look Once v11)
* **Image Processing:** OpenCV

### Database
* **RDBMS:** MySQL 8.x

### DevOps & Infrastructure (Phase 3 Engine)
* **Containerization:** Docker, Docker Compose
* **CI/CD:** GitHub Actions
* **Web Server / Reverse Proxy:** Nginx
* **OS Environment:** Linux (Ubuntu 24.04 LTS)

---

## 📂 3. Repository Structure

```text
VisionFlow/
├── visionflow-backend/     # Java / Spring Boot 메인 백엔드 애플리케이션
├── visionflow-ai/          # Python / FastAPI & YOLO 기반 AI 엔진 API 서버
├── visionflow-db/          # MySQL DDL/DML 스크립트 및 ERD 문서화 관리
└── visionflow-devops/      # Docker Compose, Nginx, CI/CD 워크플로우 설정 파일


🎯 4. Key Features & Phase Roadmap
🏁 Phase 2: Core Service & AI Integration (7월 목표)
AI Automated Asset Logging: 사용자가 물품 사진(노트북, 모니터, 키보드 등)을 업로드하면 AI가 객체를 식별하여 카테고리별로 자동 분류 및 재고 수량을 갱신합니다.

Role-Based Access Control (RBAC): Spring Security + JWT를 이용해 일반 직원(자산 조회 및 대여 요청)과 관리자(AI 기반 자산 등록 및 마스터 데이터 제어)의 권한을 엄격히 격리합니다.

Asset History Tracking: 모든 자산의 생성, 변경, 입출고 이력을 불변(Immutable) 로그 테이블로 격리하여 추적성을 보장합니다.

🚀 Phase 3: Infrastructure & DevOps Optimization (3차 목표)
Multi-Container Orchestration: 전 부품 컨테이너화(Docker) 및 가상 네트워크 결합(Docker Compose).

Automated CI/CD Pipeline: GitHub Actions를 활용한 무중단 통합 및 배포 자동화.

Reverse Proxy & Security: Nginx를 전면에 배치하여 포트 숨김 처리 및 SSL(HTTPS) 라우팅 적용.

👤 Developer 포지션
개발자: [본인 이름 입력]

역할: PyvaOps 1인 팀 리더 / 기획, 백엔드 아키텍처 설계, AI 모델 파이프라인 구축, 데브옵스 인프라 엔지니어링 전과정 전담






