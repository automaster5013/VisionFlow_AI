-- 1. 초기 카테고리 데이터 삽입 (AI가 인식할 타겟 오브젝트 중심)
INSERT INTO category (category_name, description) VALUES 
('LAPTOP', '회사 지급용 및 교육용 노트북 컴퓨터'),
('MONITOR', '데스크탑 및 듀얼 모니터 디스플레이 디바이스'),
('KEYBOARD', '사내 비치용 기계식 및 멤브레인 키보드'),
('MOUSE', '사내 비치용 무선/유선 마우스');

-- 2. 초기 관리자 계정 생성 (Spring Security 테스트용, 패스워드는 임시 텍스트)
-- 실무에서는 암호화(BCrypt)된 해시값이 들어갑니다.
INSERT INTO member (username, password, name, department, role) VALUES 
('admin', '1234', '관리자', 'PyvaOps 운영팀', 'ROLE_ADMIN'),
('user01', '1234', '홍길동', '개발1팀', 'ROLE_USER');

-- 3. 초기 자산 데이터 샘플 데이터 삽입
INSERT INTO asset (asset_code, category_id, asset_name, status, location, image_url) VALUES 
('HP-OMEN-16-001', 1, 'HP 오멘 16 라이젠 AI', 'AVAILABLE', '제1직업훈련실', 'https://s3.vflow.com/images/omen16.jpg'),
('LG-27MK-05', 2, 'LG 27인치 모니터', 'AVAILABLE', '제1직업훈련실', 'https://s3.vflow.com/images/lg27.jpg');

-- 4. 최초 등록 히스토리 연동 기록 생성
INSERT INTO asset_history (asset_id, member_id, action_type, description) VALUES 
(1, 1, 'REGISTER', '시스템 초기 구동에 따른 마스터 자산 강제 등록'),
(2, 1, 'REGISTER', '시스템 초기 구동에 따른 마스터 자산 강제 등록');