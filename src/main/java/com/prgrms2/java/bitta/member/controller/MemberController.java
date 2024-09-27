package com.prgrms2.java.bitta.member.controller;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<MemberDTO> getMemberInfo(Principal principal) {
        String email = principal.getName(); // 로그인한 사용자의 이메일 가져오기
        MemberDTO memberInfo = memberService.read(email);
        return ResponseEntity.ok(memberInfo);
    }
}