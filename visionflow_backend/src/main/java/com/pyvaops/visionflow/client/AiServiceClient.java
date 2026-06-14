package com.pyvaops.visionflow.client;

import com.pyvaops.visionflow.dto.AIResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AiServiceClient {

    private final WebClient webClient;

    public AiServiceClient(@Value("${com.visionflow.ai-server-url}") String aiServerUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }

    /**
     * FastAPI 서버로 이미지를 전달하여 YOLO 분석 결과를 받아옵니다.
     */
    public AIResponseDto requestImageInference(MultipartFile imageFile, Long memberId) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();

            // 🌟 [수정 포인트] 에러를 유발하는 익명 클래스(new ByteArrayResource(){})를 지우고
            // MultipartFile의 내부 리소스 스트림을 직접 꺼내어 찔러 넣어 줍니다.
            // 이렇게 하면 컴파일러가 추가 클래스 파일($1.class)을 만들지 않아 에러가 나지 않습니다.
            Resource imageResource = imageFile.getResource();

            builder.part("image", imageResource, MediaType.IMAGE_JPEG);
            if (memberId != null) {
                builder.part("member_id", memberId.toString());
            }

            return webClient.post()
                    .uri("/api/v1/ai/predict")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(AIResponseDto.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("AI 서버와의 통신 중 에러가 발생했습니다: " + e.getMessage(), e);
        }
    }
}