package com.boradcast.aianchor.service;

import com.boradcast.aianchor.dto.GenerateRequestDTO;
import com.boradcast.aianchor.dto.GenerateResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ScriptService {

    private final RestClient openAiRestClient;

    @Value("${openai.api.key:aaa}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.temperature:0.7}")
    private double temperature;

    @Value("${openai.max-tokens:900}")
    private int maxTokens;

    public ScriptService(RestClient openAiRestClient) {
        this.openAiRestClient = openAiRestClient;
    }

    public GenerateResponseDTO generate(GenerateRequestDTO req) {
        final String mode = normalizeMode(req.getType());
        final String prompt = "릴스".equals(mode) ? buildReelsPrompt(req) : buildNewsPrompt(req);

        // 키가 placeholder면 실제 호출 대신 더미 스크립트 반환(테스트 편의)
        if ("aaa".equals(apiKey) || apiKey.isBlank()) {
            String stub = "(테스트용: 실제 키 미설정)\n\n" +
                    "=== 프롬프트 미리보기 ===\n" + prompt + "\n=== /프롬프트 ===";
            log.info("[IAnchor] 키 미설정으로 더미 스크립트 반환");
            return new GenerateResponseDTO(stub, mode);
        }

        // OpenAI Chat Completions 호출 (동기)
        String script = callOpenAiChat(prompt, model, temperature, maxTokens);

        return new GenerateResponseDTO(script, mode);
    }

    // ----- 프롬프트 -----
    private String buildNewsPrompt(GenerateRequestDTO r) {
        String requirements = defaultIfBlank(r.getRequirements(), "요구사항 없음");
        return """
    당신은 뉴스 앵커 스크립트를 작성하는 전문 작가다.

    [언어 규칙]
    - 요구사항에 특정 언어가 명시되면 그 언어로 작성한다.
    - 명시가 없으면 제목/내용의 주요 언어를 자동으로 추론해, 그 언어로 동일하게 작성한다.
    - 날짜·숫자·단위·고유명사 표기는 해당 언어 관례에 맞춘다.

    [목표]
    - 아래 입력(제목/내용/요구사항)을 바탕으로 60~90초 분량의 뉴스 리포트 대본을 완성한다.
    
    [톤·문체 규칙]
    - 방송 앵커 톤: ~습니다, ~인데요, ~전해집니다 등 종결어미를 사용한다.
    - 문장은 짧게 끊지 말고, 연결어(그러면서, 다만, 이어서 등)를 활용해 자연스럽게 이어간다.
    - 선정적 표현·과장 금지. 평가적 표현은 '의견이 있습니다', '호불호가 갈립니다'처럼 중립적으로.

    [출력 규칙]
    - 구조:
      1) 오프닝 헤드라인 1–2문장
      2) 본문: 핵심 사실 3–5개(숫자·날짜·주체 명확), 5-6문단
      3) 클로징: 요약 + 전망/주의/다음조치
    - 문장은 호흡감 있게 전체 길이 120단어(또는 650자) 초과 금지.
    - 마크다운/이모지 금지, 순수 텍스트만.
    - 마지막 줄에 [OST 키워드] 5~8개를 쉼표로 구분해 제시.
    - 수식·예시는 생략

    [입력]
    - 유형: 뉴스
    - 제목: %s
    - 내용: %s
    - 요구사항: %s

    [출력]
    위 규칙을 충실히 따른 완성된 뉴스 대본 전체.
    """.formatted(safe(r.getTitle()), safe(r.getContent()), requirements);
    }


    private String buildReelsPrompt(GenerateRequestDTO r) {
        String requirements = defaultIfBlank(r.getRequirements(), "요구사항 없음");
        return """
    당신은 15~30초짜리 쇼츠/릴스 대본을 쓰는 라이터다.

    [언어 규칙]
    - 요구사항에 특정 언어가 명시되면 그 언어로 작성한다.
    - 명시가 없으면 제목/내용의 주요 언어를 자동으로 추론해, 그 언어로 동일하게 작성한다.
    - 숫자/단위/해시태그 표기는 해당 언어 관례에 맞춘다.

    [목표]
    - 아래 입력(제목/내용/요구사항)을 바탕으로 짧고 임팩트 있는 릴스 대본을 완성한다.

    [출력 규칙]
    - 구조: [후킹 1문장] → [핵심 5문장)] → [콜투액션 1문장]
    - 각 문장은 7~15자 내외로 간결하게.
    - 과도한 약속·허위 금지, 필요 시 주의 문구는 짧게.
    - 마지막에 [해시태그] 6~10개를 줄바꿈으로 구분해 제시.
    - 마크다운/이모지 금지, 순수 텍스트만.

    [입력]
    - 유형: 릴스
    - 제목: %s
    - 내용: %s
    - 요구사항: %s

    [출력]
    위 규칙을 충실히 따른 완성된 릴스 대본 전체.
    """.formatted(safe(r.getTitle()), safe(r.getContent()), requirements);
    }


    // ----- OpenAI 호출 -----
    private String callOpenAiChat(String prompt, String model, double temperature, int maxTokens) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", temperature,
                "max_tokens", maxTokens
        );

        try {
            OpenAiChatResponse res = openAiRestClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(OpenAiChatResponse.class);

            return Optional.ofNullable(res)
                    .filter(r -> r.getChoices() != null && !r.getChoices().isEmpty())
                    .map(r -> r.getChoices().get(0))
                    .map(OpenAiChatResponse.Choice::getMessage)
                    .map(OpenAiChatResponse.Message::getContent)
                    .orElseThrow(() -> new RuntimeException("No content returned from OpenAI"));
        } catch (Exception e) {
            throw new RuntimeException("OpenAI API error: " + e.getMessage(), e);
        }
    }

    // ----- 유틸 & 응답 DTO -----
    private String safe(String s) { return s == null ? "" : s.trim(); }
    private String defaultIfBlank(String s, String def) { return (s == null || s.isBlank()) ? def : s.trim(); }

    @Data @NoArgsConstructor
    public static class OpenAiChatResponse {
        private java.util.List<Choice> choices;
        @Data @NoArgsConstructor public static class Choice { private Message message; }
        @Data @NoArgsConstructor public static class Message { private String role; private String content; }
    }

    private String normalizeMode(String type) {
        if (type == null) return "뉴스";
        String t = type.trim().toLowerCase();
        if (t.contains("릴스") || t.contains("short") || t.contains("쇼츠")) return "릴스";
        return "뉴스";
    }
}
