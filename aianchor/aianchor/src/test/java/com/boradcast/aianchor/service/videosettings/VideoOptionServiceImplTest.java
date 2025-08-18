// VideoOptionServiceImplTest.java
// 테스트 코드

package com.boradcast.aianchor.service.videosettings;

import com.boradcast.aianchor.config.videosettings.DidApiConfig;
import com.boradcast.aianchor.dto.videosettings.VideoOptionDTO;
import com.boradcast.aianchor.service.videosettings.VideoOptionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.http.HttpEntity;

// VideoOptionServiceImpl 단위 테스트 클래스
// Mockito 사용, 의존성 Mocking 및 서비스 로직 검증
@ExtendWith(MockitoExtension.class)     // Mockito 확장 사용, Mock 객체 주입 활성화
public class VideoOptionServiceImplTest {

    @InjectMocks    // 테스트 대상인 VideoOptionServiceImpl 인스턴스에 Mock 객체 주입
    private VideoOptionServiceImpl videoOptionService;  // 테스트할 서비스 구현체 필드

    @Mock       // DidApiConfig Mock 객체 생성
    private DidApiConfig didApiConfig;      // Mocking할 DidApiConfig 필드

    @Mock       // RestTemplate Mock 객체 생성
    private RestTemplate restTemplate;      // Mocking할 RestTemplate 필드

    private VideoOptionDTO testDto;         // 테스트에 사용할 VideoOptionDTO 필드

    @BeforeEach         // 각 테스트 메소드 실행 전 초기화 메소드 선언
    void setUp() {      // 테스트 환경 설정 메소드
        testDto = new VideoOptionDTO();             // VideoOptionDTO 객체 생성
        testDto.setScriptText("테스트 스크립트");     // 스크립트 텍스트
        testDto.setAvatarId("test_avatar");         // 아바타 ID
        testDto.setVoiceId("test_voice");           // 목소리 ID
        testDto.setStyle("test_style");             // 스타일
        testDto.setLanguageCode("ko-KR");           // 언어 코드
        testDto.setBackgroundOption("#000000");     // 배경 옵션
    }

    @Test
    // generateVideo 메소드가 더미 URL 반환 테스트
    void generateVideo_shouldReturnDummyUrl_whenCalled() {
        // Given (준비): Mock 객체 동작 설정
        when(didApiConfig.getApiKey()).thenReturn("dummy_api_key");           // didApiConfig.getApiKey() 호출 시 "dummy_api_key" 반환

        // Mock RestTemplate response
        Map<String, String> mockResponseBody = new HashMap<>();
        mockResponseBody.put("id", "dummy_talk_id"); // D-ID API는 'id'를 반환
        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(
                any(URI.class),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(mockResponseEntity);

        // When (실행): 테스트 대상 메소드 호출
        String resultId = videoOptionService.generateVideo(testDto);               // generateVideo 메소드 호출 및 결과 저장

        // Then (검증): 결과 예상과 일치 검증
        assertEquals("dummy_talk_id", resultId);    // 반환된 ID가 예상 더미 ID와 같은지 검증
    }
}
