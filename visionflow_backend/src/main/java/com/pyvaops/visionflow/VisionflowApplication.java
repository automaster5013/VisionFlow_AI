package com.pyvaops.visionflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisionflowApplication {

    public static void main(String[] args) {
        // 이 메서드가 호출되면서 내장 톰캣(Tomcat) 서버가 켜지고 
        // 우리가 만든 서비스와 클라이언트 컴포넌트들이 활성화됩니다.
        SpringApplication.run(VisionflowApplication.class, args);
    }
}