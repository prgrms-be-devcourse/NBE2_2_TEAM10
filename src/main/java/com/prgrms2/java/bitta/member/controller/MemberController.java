package com.prgrms2.java.bitta.member.controller;

import com.prgrms2.java.bitta.global.exception.AuthenticationException;
import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.entity.Role;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import com.prgrms2.java.bitta.member.service.MemberService;
import com.prgrms2.java.bitta.token.dto.TokenResponseDto;
import com.prgrms2.java.bitta.global.util.AuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;

@Tag(name = "회원 API 컨트롤러", description = "회원와 관련된 REST API를 제공하는 컨틀롤러입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberProvider memberProvider;

    @Operation(
            summary = "테스트",
            description = "SecurityUtil 로부터 회원아이디를 얻는 테스트용 API입니다."
    )
    @PostMapping("/test")
    public String test() {
        return AuthenticationProvider.getUsername();
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.Login loginDto) {
        TokenResponseDto tokenResponseDto = memberService.validate(loginDto);

        return ResponseEntity.ok(Map.of("message", "로그인에 성공했습니다."
                , "result", tokenResponseDto));
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
    @PostMapping
    public String register(@RequestBody MemberRequestDto.Register registerDto, Model model) {
        memberService.insert(registerDto);

        // 회원가입 후 username 추가
        model.addAttribute("username", registerDto.getUsername());
        model.addAttribute("successMessage", "회원가입에 성공했습니다.");

        return "redirect:/member/join-complete";

        //return ResponseEntity.ok(Map.of("message", "회원가입에 성공했습니다."));
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
    public ResponseEntity<?> read(@PathVariable("id") @Min(1) Long id) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        return ResponseEntity.ok(Map.of("message", "회원을 성공적으로 조회했습니다."
                , "result", memberService.read(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") @Min(1) Long id
            , @RequestBody @Valid MemberRequestDto.ChangePassword changePasswordDto) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        changePasswordDto.setId(id);

        memberService.changePassword(changePasswordDto);

        return ResponseEntity.ok().body(Map.of("message", "비밀번호가 수정되었습니다."));
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
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> modify(@PathVariable("id") @Min(1) Long id, @RequestPart(value = "member") @Valid MemberRequestDto.Modify modifyDto
            , @RequestPart(value = "file", required = false) MultipartFile file) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        modifyDto.setId(id);

        if (!file.isEmpty()) {
            System.out.println("파일이 있습니다.");
            memberService.update(modifyDto, file);
        } else {
            System.out.println("파일이 비어있습니다.");
            memberService.update(modifyDto);
        }

        return ResponseEntity.ok().body(Map.of("message", "회원이 수정되었습니다."));
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
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!checkPermission(id)) {
            throw AuthenticationException.CANNOT_ACCESS.get();
        }

        memberService.delete(id);
        log.info("회원 삭제 완료 - 사용자 ID: {}", id);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    private boolean checkPermission(Long id) {
        if (AuthenticationProvider.getRoles() == Role.ROLE_ADMIN) {
            return true;
        }

        return memberService.checkAuthority(id, AuthenticationProvider.getUsername());
    }
}
