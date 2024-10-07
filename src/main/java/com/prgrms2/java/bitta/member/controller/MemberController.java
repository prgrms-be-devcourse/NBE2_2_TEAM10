package com.prgrms2.java.bitta.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignInDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.exception.NoChangeException;
import com.prgrms2.java.bitta.member.service.MemberService;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.SecurityUtil;
import com.prgrms2.java.bitta.security.dto.RefreshTokenRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "회원 API 컨트롤러", description = "회원와 관련된 REST API를 제공하는 컨틀롤러입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "테스트",
            description = "SecurityUtil 로부터 회원아이디를 얻는 테스트용 API입니다."
    )
    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    @Operation(
            summary = "로그인",
            description = "로그인 후 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "401",
                            description = "로그인 실패",
                            content = @Content)
            }
    )
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDTO signInDTO) {
        JwtToken jwtToken = memberService.signIn(signInDTO.getUsername(), signInDTO.getPassword());
        log.info("로그인 성공: username={}, accessToken={}", signInDTO.getUsername(), jwtToken.getAccessToken());
        return ResponseEntity.ok(jwtToken);
    }

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = MEMBER_SUCCESS_SIGN_UP))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원가입 실패",
                            content = @Content)
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<MemberDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        return ResponseEntity.ok(memberService.signUp(signUpDTO));
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 ID를 사용해 회원 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 조회 성공",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원 정보 조회 실패",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원의 정보를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 수정 성공",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "304",
                            description = "변경된 내용 없음",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "프로필 이미지 처리 오류",
                            content = @Content)
            }
    )
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<MemberDTO> updateMemberById(@PathVariable Long id,
                                                      @RequestParam("dto") String dtoJson,
                                                      @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                                      @RequestParam(value = "removeProfileImage", required = false, defaultValue = "false") Boolean removeProfileImage) {
        try {
            MemberDTO memberDTO = objectMapper.readValue(dtoJson, MemberDTO.class);
            MemberDTO updatedMember = memberService.updateMember(id, memberDTO, profileImage, removeProfileImage);
            return ResponseEntity.ok(updatedMember);
        } catch (NoChangeException e) {
            log.warn("변경된 내용이 없습니다 - 사용자 ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (IOException e) {
            log.error("프로필 이미지 처리 오류 - 사용자 ID: {}, 에러: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴를 진행합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 탈퇴 성공",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원 탈퇴 실패",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMember(id);
        log.info("회원 삭제 완료 - 사용자 ID: {}", id);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }


    private String extractToken(String bearerToken) {
        return bearerToken.substring(7);
    }

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰으로 Access 토큰을 재발급 합니다.",
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
    @PostMapping("/refresh")
    public ResponseEntity<?> reissueToken(@RequestHeader("Authorization") String bearerAccessToken,
                                          @RequestBody RefreshTokenRequestDTO request) {
        try {
            String accessToken = extractToken(bearerAccessToken);
            JwtToken newToken = memberService.reissueToken(accessToken, request.getRefreshToken());

            if (newToken == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "액세스 토큰이 유효합니다."));
            }

            log.info("토큰 재발급 성공 - 새로운 액세스 토큰: {}", newToken.getAccessToken());
            return ResponseEntity.ok(newToken);
        } catch (AuthenticationException e) {
            log.error("토큰 재발급 실패 - 이유: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
