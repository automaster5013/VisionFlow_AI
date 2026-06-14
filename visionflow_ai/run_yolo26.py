import os
from ultralytics import YOLO

def main():
    # 1. 모델 로드 (yolo26n.pt)
    model = YOLO("yolo26n.pt")

    # 2. 테스트용 이미지 경로
    # 윈도우에서는 경로에 백슬래시(\)가 쓰이므로 r을 붙여 raw string으로 처리
    image_path = r"C:\PyvaOps\VisionFlow\visionflow_ai\test_image.jpg" 

    if not os.path.exists(image_path):
        print(f"에러: {image_path} 파일이 존재하지 않습니다. 테스트용 이미지 경로를 확인해 주세요.")
        return

    # 3. 이미지 예측 수행
    results = model(image_path)

    # 4. 결과 저장 및 확인 (기본적으로 'runs/detect/predict' 폴더에 저장)
    for result in results:
        result.save()  # 결과 이미지 저장
        result.show()  # 화면에 결과 띄우기

if __name__ == "__main__":
    main()