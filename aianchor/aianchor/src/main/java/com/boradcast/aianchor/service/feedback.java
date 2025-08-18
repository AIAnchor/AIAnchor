package com.boradcast.aianchor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

// 메일을 생성하고 발송하는 역할
// @service 어노테이션을 사용하여 스프링의 서비스 컴포넌트(스프링 컨테이너에 Bean 등록)로 등록
@Service
public class feedback {

    // JavaMailSender는 스프링에서 메일을 보내기 위한 인터페이스
    // @Autowired 어노테이션을 사용하여 스프링 컨테이너에서 자동으로 주입
    @Autowired
    private JavaMailSender mailSender;


    // 피드백을 수신할 관리자 이메일 주소
    // application.properties 또는 application.yml 파일에서 설정된 값을 주입받기 위한 어노테이션
    @Value("${feedback.recipient.email}")
    private String recipientEmail;

    /**
     * 피드백 이메일을 생성하고 발송하는 메소드
     * @param subject 이메일 제목
     * @param body 이메일 본문
     */
    public void sendFeedbackEmail(String subject, String body) {
        // MimeMessage 객체를 생성하여 이메일 메시지를 구성
        MimeMessage message = mailSender.createMimeMessage();

        // true는 multipart 메시지를 의미하며, 첨부파일을 지원
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipientEmail);       // 수신자 이메일 주소
            helper.setSubject(subject);         // 이메일 제목

            helper.setText(body, false);  // false는 HTML이 아님을 의미

            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("메일 발송 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }
}