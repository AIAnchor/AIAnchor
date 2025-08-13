package com.boradcast.aianchor.controller;

import com.boradcast.aianchor.dto.GenerateRequestDTO;
import com.boradcast.aianchor.dto.GenerateResponseDTO;
import com.boradcast.aianchor.service.ScriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScriptController {

    private final ScriptService scriptService;

    @PostMapping("/generate")
    public ResponseEntity<GenerateResponseDTO> generate(@RequestBody GenerateRequestDTO request) {
        return ResponseEntity.ok(scriptService.generate(request));
    }
}
