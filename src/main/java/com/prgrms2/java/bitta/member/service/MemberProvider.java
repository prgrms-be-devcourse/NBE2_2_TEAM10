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
        return memberRepository.findById(id).orElse(null);
    }
}
