// VideoOptionControllerTest.java
// 테스트 코드

package com.boradcast.aianchor.controller.videosettings;

import com.boradcast.aianchor.dto.videosettings.VideoOptionDTO;
import com.boradcast.aianchor.service.videosettings.VideoOptionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// ideoOptionController 통합 테스트 클래스
// MockMvc 사용, 컨트롤러의 HTTP 요청 처리 검증
@WebMvcTest(VideoOptionController.class)    // 웹 계층 테스트 수행
public class VideoOptionControllerTest {

    @Autowired      // MockMvc 객체 주입
    private MockMvc mockMvc;    // HTTP 요청 시뮬레이션을 위한 MockMvc 필드

    @MockBean       // VideoOptionService Mock 객체 생성, Spring 컨텍스트에 등록
    private VideoOptionService videoOptionService;      // Mocking할 VideoOptionService 필드

    @Autowired      // ObjectMapper 객체 주입
    private ObjectMapper objectMapper;      // JSON 직렬화/역직렬화를 위한 ObjectMapper 필드

    @Test
    // 유효한 요청 시 영상 URL 반환 테스트
    void generateVideo_shouldReturnVideoUrl_whenValidRequest() throws Exception {
        // Given (준비): 테스트에 필요한 데이터와 Mock 객체 동작 설정
        VideoOptionDTO requestDto = new VideoOptionDTO();   // VideoOptionDTO 객체 생성
        requestDto.setScriptText("테스트 스크립트");  // 스크립트 텍스트
        requestDto.setAvatarId("test_avatar");      // 아바타 ID
        requestDto.setVoiceId("test_voice");        // 목소리 ID
        requestDto.setStyle("test_style");          // 스타일
        requestDto.setLanguageCode("ko-KR");        // 언어 코드
        requestDto.setBackgroundOption("#000000");  // 배경 옵션

        String expectedUrl = "https://example.com/generated_video_from_controller";         // 예상 반환 URL

        // videoOptionService.generateVideo(any(VideoOptionDTO.class)) 호출 시 expectedUrl 반환
        when(videoOptionService.generateVideo(any(VideoOptionDTO.class))).thenReturn(expectedUrl);

        // When (실행): HTTP POST 요청 시뮬레이션
        mockMvc.perform(post("/api/video/generate")          // /api/video/generate 경로로 POST 요청 시작
                .contentType(MediaType.APPLICATION_JSON)                // 요청의 Content-Type JSON으로 설정
                .content(objectMapper.writeValueAsString(requestDto)))  // requestDto를 JSON 문자열로 변환하여 요청 본문에 담기
                .andExpect(status().isOk())                             // 응답 상태 코드 200 OK 검증
                .andExpect(content().string(expectedUrl));              // 응답 본문 내용 예상 URL과 일치 검증
    }
}