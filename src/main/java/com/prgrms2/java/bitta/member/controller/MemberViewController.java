package com.prgrms2.java.bitta.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberViewController {
    // 로그인
    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login";
    }

    // 회원가입
    @GetMapping("/join")
    public String showJoinPage() {
        return "member/join";
    }

    // 회원가입완료
    @GetMapping("/join-complete")
    public String JoinCompletePage() {
        return "member/join-complete";
    }

    // 회원정보수정
    @GetMapping("/myinfo")
    public String modifyMyPage() {
        return "member/myinfo";
    }

    // 회원정보조회
    @GetMapping("/mypage")
    public String showMyPage() {
        return "member/mypage";
    }

    // 비밀번호찾기
    @GetMapping("/find")
    public String findMyPW() {
        return "member/find";
    }
}
