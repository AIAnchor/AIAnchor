// AianchorApplication.java

package com.boradcast.aianchor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AianchorApplication {
    // main
    public static void main(String[] args) {
        try {
            SpringApplication.run(AianchorApplication.class, args);
        } catch (Exception e) {
            log.error("!!!예외 발생: {}", e.getMessage(), e);
        }
    }
}
