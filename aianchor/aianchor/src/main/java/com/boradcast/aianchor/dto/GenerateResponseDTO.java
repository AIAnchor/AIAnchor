package com.boradcast.aianchor.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GenerateResponseDTO {
    private String script; // 최종 스크립트
    private String mode;   // "뉴스" | "릴스"
}
