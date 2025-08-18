package com.boradcast.aianchor.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GenerateRequestDTO {
    private String title;         // 영상제목(주제)
    private String content;       // 내용
    private String requirements;  // 추가 요구사항
    private String type;          // "뉴스" | "릴스"
}
