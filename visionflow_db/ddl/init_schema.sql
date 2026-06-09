-- 1. 데이터베이스(스키마) 생성 및 선택
CREATE DATABASE IF NOT EXISTS visionflow_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE visionflow_db;

-- 기존 테이블이 존재할 경우 삭제 (순서 주의: 자식 테이블부터 삭제)
DROP TABLE IF EXISTS asset_history;
DROP TABLE IF EXISTS asset;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS member;

-- ==========================================
-- 2. MEMBER (사용자 및 관리자 테이블)
-- ==========================================
CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT COMMENT '사용자 고유 일련번호',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '로그인 ID',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호 (Spring Security 바인딩)',
    name VARCHAR(50) NOT NULL COMMENT '사용자 본명',
    department VARCHAR(100) COMMENT '소속 부서',
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '권한 (ROLE_USER, ROLE_ADMIN)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '계정 생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '계정 수정일',
    PRIMARY KEY (member_id)
) ENGINE=InnoDB COMMENT='시스템 이용자 및 관리자 정보';

-- ==========================================
-- 3. CATEGORY (자산 카테고리 테이블)
-- ==========================================
CREATE TABLE category (
    category_id INT AUTO_INCREMENT COMMENT '카테고리 고유 일련번호',
    category_name VARCHAR(50) NOT NULL UNIQUE COMMENT '카테고리명 (예: LAPTOP, MONITOR, KEYBOARD)',
    description VARCHAR(255) COMMENT '카테고리 설명',
    PRIMARY KEY (category_id)
) ENGINE=InnoDB COMMENT='자산 분류 정보';

-- ==========================================
-- 4. ASSET (스마트 자산/재고 메인 테이블)
-- ==========================================
CREATE TABLE asset (
    asset_id BIGINT AUTO_INCREMENT COMMENT '자산 고유 일련번호',
    asset_code VARCHAR(100) NOT NULL UNIQUE COMMENT 'AI 혹은 바코드로 식별할 고유 자산 코드/일련번호',
    category_id INT NOT NULL COMMENT '카테고리 일련번호 (FK)',
    asset_name VARCHAR(100) NOT NULL COMMENT '자산 상세 명칭 (예: HP 오멘 16)',
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '자산 상태 (AVAILABLE: 이용가능, RENTED: 대여중, BROKEN: 폐기/수리중)',
    location VARCHAR(100) COMMENT '현재 보관 위치 (예: 제1강의실, 창고A)',
    image_url VARCHAR(512) COMMENT 'AWS S3 등에 업로드된 자산 이미지 경로',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '최초 등록일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종 정보 수정일',
    PRIMARY KEY (asset_id),
    CONSTRAINT fk_asset_category FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE RESTRICT
) ENGINE=InnoDB COMMENT='물류 및 사내 자산 메인 관리 테이블';

-- 검색 최적화를 위한 인덱스 추가 (카테고리별 자산 조회, 상태별 자산 조회용)
CREATE INDEX idx_asset_status ON asset(status);
CREATE INDEX idx_asset_code ON asset(asset_code);

-- ==========================================
-- 5. ASSET_HISTORY (자산 변동/입출고 이력 테이블)
-- ==========================================
CREATE TABLE asset_history (
    history_id BIGINT AUTO_INCREMENT COMMENT '이력 고유 일련번호',
    asset_id BIGINT NOT NULL COMMENT '자산 일련번호 (FK)',
    member_id BIGINT NOT NULL COMMENT '작업 수행자 일련번호 (FK)',
    action_type VARCHAR(20) NOT NULL COMMENT '변동 종류 (REGISTER: 신규등록, UPDATE: 정보수정, RENT: 대여, RETURN: 반납, DISPOSAL: 폐기)',
    description TEXT COMMENT '변동 사유 및 상세 내용 (예: AI 자동 객체 인식을 통한 신규 등록)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '이력 기록 시간',
    PRIMARY KEY (history_id),
    CONSTRAINT fk_history_asset FOREIGN KEY (asset_id) REFERENCES asset (asset_id) ON DELETE CASCADE,
    CONSTRAINT fk_history_member FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE RESTRICT
) ENGINE=InnoDB COMMENT='자산 변동 및 데이터 입출고 히스토리 (불변 로그)';

-- 변동 이력 역순(최신순) 조회를 위한 인덱스 추가
CREATE INDEX idx_history_created_at ON asset_history(created_at DESC);




