package com.prgrms2.java.bitta.member.controller;


import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignInDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.service.MemberService;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "회원 API 컨트롤러", description = "회원와 관련된 REST API를 제공하는 컨틀롤러입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "로그인",
            description = "아이디와 비밀번호를 검증하고, 토큰을 반환합니다."
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
            summary = "테스트",
            description = "SecurityUtil 로부터 회원아이디를 얻는 테스트용 API입니다."
    )
    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
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
                                    schema = @Schema(example = MEMBER_SUCCESS_SIGN_UP)
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
            summary = "프로필 이미지 수정",
            description = "회원의 프로필 이미지를 수정합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로필 이미지를 성공적으로 수정했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = MEMBER_SUCCESS_UPDATE_PROFILE_IMAGE)
                            )
                    ),
            }
    )
    @Parameter(
            name = "id",
            description = "프로필 이미지를 수정할 회원의 ID",
            required = true,
            example = "1",
            schema = @Schema(type = "integer")
    )

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMemberById(@PathVariable Long id,
                                                      @RequestBody MemberDTO memberDTO,
                                                      @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                                      @RequestParam(value = "removeProfileImage", defaultValue = "false") boolean removeProfileImage) {
        MemberDTO updatedMember = memberService.updateMember(id, memberDTO, profileImage, removeProfileImage);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원 삭제가 완료되었습니다.");
    }
}
