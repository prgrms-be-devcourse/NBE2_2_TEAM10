package com.prgrms2.java.bitta.member.controller;

import com.prgrms2.java.bitta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MembeViewController {
    private final MemberRepository memberRepository;

    @GetMapping("Member")
    String viewMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "member.html";
    }
}
