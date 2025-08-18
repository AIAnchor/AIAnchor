// VideoOptionService.java
// 인터페이스

package com.boradcast.aianchor.service.videosettings;

import com.boradcast.aianchor.dto.videosettings.VideoOptionDTO;

// 인터페이스 선언 (AI 아바타 영상 생성을 위한 서비스 인터페이스)
// 다양한 AI 영상 API(d-id, HeyGen 등)에 대한 추상화 제공 - 여러 API에 대한 공통 기능 정의
public interface VideoOptionService {

    // 영상 생성을 위한 추상 메소드 정의 (사용자 설정에 따라 AI 아바타 영상 생성)
    // * @param settingsDto:   사용자가 선택한 영상 생성 설정 (아바타, 목소리, 말투, 언어 등)
    // * @return:              생성된 영상의 URL or 작업 ID (비동기 처리 시)
    String generateVideo(VideoOptionDTO settingsDto);
}
