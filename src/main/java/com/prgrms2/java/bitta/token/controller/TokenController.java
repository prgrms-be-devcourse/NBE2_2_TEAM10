package com.prgrms2.java.bitta.token.controller;

import com.prgrms2.java.bitta.token.dto.TokenRequestDto;
import com.prgrms2.java.bitta.token.dto.TokenResponseDto;
import com.prgrms2.java.bitta.token.util.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {
    private final TokenProvider tokenProvider;

    @Operation(
            summary = "리프레시 토큰 재발급",
            description = "리프레시 토큰으로 액세스 토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰을 성공적으로 재발급했습니다.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "유효하지 않은 리프레시 토큰",
                            content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto requestDto) {
        TokenResponseDto tokenResponseDto = tokenProvider.reissue(requestDto.getAccessToken(), requestDto.getRefreshToken());

        return ResponseEntity.ok(Map.of("message", "토큰을 재발행했습니다."
                , "accessToken", tokenResponseDto.getAccessToken()
                , "refreshToken", tokenResponseDto.getRefreshToken()));
    }

    @GetMapping
    public ResponseEntity<?> handleError(@RequestParam(value = "code", required = true) int code) {
        if (code == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "인증 자격 정보가 유효하지 않습니다."));
        }

        if (code == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "토큰이 유효하지 않거나 접근 권한이 없습니다."));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "잘못된 접근 경로로 접근하였습니다."));
    }
}
