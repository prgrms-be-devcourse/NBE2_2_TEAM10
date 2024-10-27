package com.prgrms2.java.bitta.member.controller;

import com.prgrms2.java.bitta.member.dto.JoinDTO;
import com.prgrms2.java.bitta.member.service.JoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);

        return "ok";
    }
}