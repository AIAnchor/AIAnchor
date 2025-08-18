// VideoOptionDTO.java

package com.boradcast.aianchor.dto.videosettings;

import lombok.Data;

// 사용자로부터 전달받는 영상 생성 설정을 담는 DTO(Data Transfer Object) 클래스
// 프론트엔드에서 백엔드로 전송되는 데이터 구조화
@Data
public class VideoOptionDTO {

    private String scriptText;          // 사용자가 입력한 스크립트 텍스트
    private String avatarId;            // 선택된 아바타 ID or URL
    private String voiceId;             // 선택된 목소리 ID
    private String style;               // 선택된 말투(감정)
    private String languageCode;        // 선택된 언어 코드
    private String backgroundOption;    // 선택된 배경 옵션(단색 코드, 이미지 URL 등)
}
