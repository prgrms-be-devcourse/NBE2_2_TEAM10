package com.prgrms2.java.bitta.member.controller;

import com.prgrms2.java.bitta.global.exception.AuthenticationException;
import com.prgrms2.java.bitta.member.dto.CustomUserDetails;
import com.prgrms2.java.bitta.member.dto.MemberRequestDTO;
import com.prgrms2.java.bitta.member.dto.MemberResponseDTO;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import com.prgrms2.java.bitta.member.service.MemberService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.prgrms2.java.bitta.global.constants.ApiResponses.*;
@Tag(name = "회원 API 컨트롤러", description = "회원과 관련된 REST API를 제공하는 컨트롤러입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원가입 실패",
                            content = @Content)
            }
    )
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid MemberRequestDTO.Join joinDTO) {
        memberService.join(joinDTO);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 회원의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDTO> read(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(memberService.read(userDetails.getUsername()));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인한 회원의 비밀번호를 변경합니다."
    )
    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid MemberRequestDTO.ChangePassword changePasswordDTO
    ) {
        memberService.changePassword(userDetails.getUsername(), changePasswordDTO);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "현재 로그인한 회원의 정보를 수정합니다."
    )
    @PutMapping(value = "/me")
    public ResponseEntity<String> modify(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid MemberRequestDTO.Modify modifyDTO
    ) {
        memberService.update(userDetails.getUsername(), modifyDTO);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    @Operation(
            summary = "프로필 이미지 수정",
            description = "현재 로그인한 회원의 프로필 이미지를 수정합니다."
    )
    @PutMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> modifyWithImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "member") @Valid MemberRequestDTO.Modify modifyDTO,
            @RequestPart(value = "file") MultipartFile file
    ) {
        memberService.update(userDetails.getUsername(), modifyDTO, file);
        return ResponseEntity.ok("프로필 이미지가 수정되었습니다.");
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 회원의 탈퇴를 진행합니다."
    )
    @DeleteMapping("/me")
    public ResponseEntity<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.delete(userDetails.getUsername());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}