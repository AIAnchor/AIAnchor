package com.boradcast.aianchor.controller;

import com.boradcast.aianchor.service.feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자로부터 피드백을 받아 이메일로 전송하는 역할
// @RestController 어노테이션을 사용하여 RESTful 웹 서비스의 컨트롤러로 등록
// @RequestMapping 어노테이션을 사용하여 기본 URL 경로를 설정
@RestController
@RequestMapping("/api/feedback")
public class feedbackController {

    // 피드백 서비스 클래스에 대한 의존성을 주입
    @Autowired
    private feedback feedbackService;

    // 피드백을 수집하고 이메일로 전송하는 엔드포인트
    // @PostMapping 어노테이션을 사용하여 HTTP POST 요청을 처리
    @PostMapping("/send")
    public ResponseEntity<String> sendFeedback(
            @RequestParam String subject,
            @RequestParam String body) {
        try {
            feedbackService.sendFeedbackEmail(subject, body);
            return ResponseEntity.ok("소중한 의견 감사합니다. 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("메일 전송 중 서버 오류가 발생했습니다.");
        }
    }
}