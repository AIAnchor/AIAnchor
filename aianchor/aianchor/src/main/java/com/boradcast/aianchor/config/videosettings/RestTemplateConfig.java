// RestTemplateConfig.java

package com.boradcast.aianchor.config.videosettings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// RestTemplate 빈 설정을 위한 Configuration 클래스
//애플리케이션 전반에서 HTTP 통신에 사용될 RestTemplate 빈 정의
@Configuration
public class RestTemplateConfig {

    // 다른 컴포넌트에서 HTTP 요청을 보내는 데 사용
    // * @return:     RestTemplate RestTemplate 인스턴트
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
