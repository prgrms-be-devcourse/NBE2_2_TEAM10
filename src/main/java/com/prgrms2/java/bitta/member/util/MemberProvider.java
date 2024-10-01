package com.prgrms2.java.bitta.member.util;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProvider {
    private final MemberRepository memberRepository;

    public Member getById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
}
