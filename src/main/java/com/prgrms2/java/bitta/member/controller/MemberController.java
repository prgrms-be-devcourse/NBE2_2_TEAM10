package com.prgrms2.java.bitta.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignInDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.service.MemberService;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.JwtTokenProvider;
import com.prgrms2.java.bitta.security.SecurityUtil;
import com.prgrms2.java.bitta.security.dto.RefreshTokenRequestDTO;
import com.prgrms2.java.bitta.security.exception.InvalidTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "회원 API 컨트롤러", description = "회원와 관련된 REST API를 제공하는 컨틀롤러입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

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
            description = "아이디와 비밀번호를 검증하고, 토큰을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인이 성공적으로 완료되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtToken.class)
                            )
                    )
            }
    )
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDTO signInDTO) {
        String username = signInDTO.getUsername();
        String password = signInDTO.getPassword();
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @Operation(
            summary = "회원가입",
            description = "회원을 등록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원을 성공적으로 등록했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberDTO.class, example = MEMBER_SUCCESS_SIGN_UP)
                            )
                    )
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<MemberDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        MemberDTO savedMember = memberService.signUp(signUpDTO);
        return ResponseEntity.ok(savedMember);
    }

    @Operation(
            summary = "회원 조회",
            description = "회원 ID로 회원 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보를 성공적으로 조회했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberDTO.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @Operation(
            summary = "회원 수정",
            description = "회원 ID로 회원 정보를 수정합니다. 프로필 이미지를 업데이트할 수 있습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보를 성공적으로 수정했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberDTO.class)
                            )
                    )
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
        } catch (IOException e) {
            log.error("Failed to update member profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "회원 삭제",
            description = "회원 ID로 회원 정보를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보를 성공적으로 삭제했습니다.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "회원 삭제가 완료되었습니다.")
                            )
                    )
            }
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 삭제가 완료되었습니다.");
    }

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰으로 Access 토큰을 재발급 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰을 성공적으로 재발급했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtToken.class)  // JwtToken 클래스 사용
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "유효하지 않은 리프레시 토큰",
                            content = @Content
                    )
            }
    )
    @PostMapping("/refresh")
    public JwtToken refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // 리프레시 토큰이 유효한지 검사
        String refreshToken = refreshTokenRequestDTO.getRefreshToken();
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        // 유효한 리프레시 토큰이면 새 액세스 토큰 발급
        return memberService.refreshToken(refreshToken);
    }
}
