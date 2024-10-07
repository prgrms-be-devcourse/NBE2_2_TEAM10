package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProvider {

    private final MemberRepository memberRepository;

    public Member getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. ID: " + id));
    }
}