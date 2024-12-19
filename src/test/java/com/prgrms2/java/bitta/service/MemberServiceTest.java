package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.member.dto.MemberRequestDTO;
import com.prgrms2.java.bitta.member.dto.MemberResponseDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void joinTest() {
        MemberRequestDTO.Join joinDTO = MemberRequestDTO.Join.builder()
                .username("test")
                .password("password")
                .nickname("nickname")
                .address("address")
                .build();

        when(memberRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        memberService.join(joinDTO);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 조회 성공 테스트")
    void readSuccess() {
        // given
        String username = "testUser";
        Member member = Member.builder()
                .username(username)
                .nickname("nickname")
                .address("서울시")
                .build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        // when
        MemberResponseDTO responseDTO = memberService.read(username);

        // then
        assertEquals(username, responseDTO.getUsername());
        assertEquals("nickname", responseDTO.getNickname());
    }
}