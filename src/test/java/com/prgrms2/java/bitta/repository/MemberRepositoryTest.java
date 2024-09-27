package com.prgrms2.java.bitta.repository;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.member.entity.Role;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@Log4j2
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 회원삽입(){
        IntStream.rangeClosed(1,10).forEach(i -> {
            Member member = Member.builder()
                    .memberName("membername" + i)
                    .password(passwordEncoder.encode("1111"))
                    .email("member"+i+"@prgrms2.com")
                    .location("서울시"+i+"번지")
                    .profilePicture(i+".jpg")
                    .role(Role.Actor)
                    .build();
            Member savedMember = memberRepository.save(member);
            assertNotNull(savedMember);
            log.info("----------"+ savedMember.toString());
        });
    }

    @Test
    public void 회원조회(){
        String email = "user1@prgrms2.com";

        Optional<Member> foundUser = memberRepository.findByEmail(email);
        assertNotNull(foundUser);
        assertEquals(email, foundUser.get().getEmail());

        log.info("Found User"+foundUser.get());
        log.info("email"+foundUser.get().getEmail());
    }

    @Test
    @Transactional
    @Commit
    public void 회원삭제(){
        String email = "user10@prgrms2.com";
        Optional<Member> foundUser = memberRepository.findByEmail(email);
        if(foundUser.isPresent()) {
            memberRepository.deleteByEmail(email);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}