// VideoOptionServiceImpl.java
// 인터페이스 구현체

package com.boradcast.aianchor.service.videosettings;

import com.boradcast.aianchor.config.videosettings.DidApiConfig;
import com.boradcast.aianchor.dto.videosettings.VideoOptionDTO;
import com.boradcast.aianchor.service.videosettings.VideoOptionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64; // Base64 인코딩을 위해 추가

// 인터페이스 구현체 (D-ID API 호출 포함, 영상 생성 기능 수행)
@Service
@Slf4j
public class VideoOptionServiceImpl implements VideoOptionService {

    private final DidApiConfig dIdApiConfig;    // D-ID API 설정 주입받기 위한 필드
    private final RestTemplate restTemplate;    // REST-ful API 호출 위한 RestTemplate 필드

    // VideoOptionServiceImpl 생성자
    // * @param dIdApiConfig:   D-ID API 설정 객체
    // * @param restTemplate:   RestTemplate 객체
    public VideoOptionServiceImpl(DidApiConfig dIdApiConfig, RestTemplate restTemplate) {
        this.dIdApiConfig = dIdApiConfig;
        this.restTemplate = restTemplate;
    }

    // 사용자의 설정에 따라 AI 아바타 영상 생성
    // * @param videoOptionDTO: 사용자가 선택한 영상 생성 설정 (아바타, 목소리, 말투, 언어 등)
    // * @return:               생성된 영상의 URL or 작업 ID (비동기 처리 시)
    @Override
    public String generateVideo(VideoOptionDTO videoOptionDTO) {
        log.info("D-ID API Key: {}", dIdApiConfig.getApiKey());
        log.info("Video Options: {}", videoOptionDTO.getScriptText());

        String didApiUrl = "https://api.d-id.com/talks";

        // 1. HTTP Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // D-ID API는 Basic Authentication을 사용하며, API 키를 사용자 이름으로 사용하고 비밀번호는 비워둡니다.
        String auth = dIdApiConfig.getApiKey() + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // 2. Request Body (JSON) 구성
        Map<String, Object> scriptMap = new HashMap<>();
        scriptMap.put("type", "text");
        scriptMap.put("input", videoOptionDTO.getScriptText());
        scriptMap.put("voice_id", videoOptionDTO.getVoiceId());
        // D-ID API는 'style'을 직접적으로 스크립트 객체 내에서 지원하지 않을 수 있습니다.
        // 필요시 D-ID API 문서를 참조하여 다른 방식으로 적용해야 합니다.
        // scriptMap.put("style", videoOptionDTO.getStyle());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("script", scriptMap);
        requestBody.put("source_url", videoOptionDTO.getAvatarId()); // avatarId가 URL이라고 가정

        // 3. HttpEntity 생성 (헤더와 바디 포함)
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 4. D-ID API 호출
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                URI.create(didApiUrl),
                requestEntity,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String talkId = (String) response.getBody().get("id");
                log.info("D-ID Talk ID: {}", talkId);
                // 실제 영상 URL은 비동기적으로 생성되므로, 여기서는 talkId를 반환하거나
                // talkId를 사용하여 영상 상태를 폴링하는 로직을 추가해야 합니다.
                // 여기서는 talkId를 반환합니다.
                return talkId;
            } else {
                log.error("D-ID API 호출 실패: 상태 코드 = {}, 응답 본문 = {}", response.getStatusCode(), response.getBody());
                return "Error: D-ID API call failed";
            }
        } catch (Exception e) {
            log.error("D-ID API 호출 중 예외 발생: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
