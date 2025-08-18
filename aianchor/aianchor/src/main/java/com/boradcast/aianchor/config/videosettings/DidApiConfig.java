// DidApiConfig.java

package com.boradcast.aianchor.config.videosettings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// D-ID API 설정 정보 관리 클래스
@Configuration
@PropertySource("classpath:application.properties")     // application.properties 파일로부터 설정 값 load
public class DidApiConfig {

    @Value("${did.api-key}")
    private String apiKey;      // D-ID API 키 저장 필드

    // API 키 반환 메소드
    public String getApiKey() {
        return apiKey;
    }
}
