package com.prgrms2.java.bitta.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberViewController {
    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login"; // member 폴더 안에 있는 login.html
    }

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String showJoinPage() {
        return "member/join"; // member 폴더 안에 있는 join.html
    }

    // 마이페이지로 이동
    @GetMapping("/mypage")
    public String showMyPage() {
        return "member/mypage"; // member 폴더 안에 있는 mypage.html
    }

    // 마이페이지로 이동
    @GetMapping("/find")
    public String findMyPW() {
        return "member/find"; // member 폴더 안에 있는 mypage.html
    }
}
