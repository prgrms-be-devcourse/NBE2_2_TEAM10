package com.prgrms2.java.bitta.token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "인증 토큰 요청 DTO", description = "토큰 재발행을 요청하는데 사용하는 DTO입니다.")
public class TokenRequestDto {
    @Schema(name = "액세스 토큰", description = "회원 데이터를 가지는 짧은 생명주기의 토큰입니다.")
    private String accessToken;

    @Schema(name = "리프레시 토큰", description = "토큰 재발행에 사용하는 긴 생명주기의 토큰입니다.")
    private String refreshToken;
}