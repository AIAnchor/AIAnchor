// VideoOptionController.java

package com.boradcast.aianchor.controller.videosettings;

import com.boradcast.aianchor.dto.videosettings.VideoOptionDTO;
import com.boradcast.aianchor.service.videosettings.VideoOptionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 영상 생성 요청 처리하는 RESTful 컨트롤러 (클라이언트 요청 처리)
// 클라이언트로부터 영상 설정을 받아 서비스 계층으로 전달
@RestController
@RequestMapping("/api/video")       // 이 컨트롤러의 모든 핸들러 메소드에 대한 기본 URL 경로 설정
public class VideoOptionController {

    private final VideoOptionService videoOptionService;        // VideoOptionService 인터페이스를 주입받기 위한 필드

    // VideoOptionController 생성자
    // * @param videoOptionService:     VideoOptionService 구현체 (생성자를 통해 VideoOptionService 의존성 주입)
    public VideoOptionController(VideoOptionService videoOptionService) {
        this.videoOptionService = videoOptionService;
    }

    // 영상 생성 요청하는 POST 엔드포인트 (클라이언트로부터 VideoOptionDTO 받아 영상 생성, 결과 반환)
    // * @param videoOptionDTO:   영상 생성에 필요한 설정 데이터 담은 DTO 파라미터
    // * @return:                 생성된 영상 URL 포함하는 HTTP 응답
    @PostMapping("/generate") // HTTP POST 요청 /api/video/generate 경로 매핑
    public ResponseEntity<String> generateVideo(@RequestBody VideoOptionDTO videoOptionDTO) {
        // VideoOptionService 호출 -> 영상 생성 요청, 결과 URL 반환
        String videoUrl = videoOptionService.generateVideo(videoOptionDTO);

        // 생성된 영상 URL 포함하는 HTTP 200 OK 응답 반환 (응답 반환 로직)
        return ResponseEntity.ok(videoUrl);     // HTTP 200 OK 상태 코드 & videoUrl 본문으로 포함하는 응답 반환
    }
}
