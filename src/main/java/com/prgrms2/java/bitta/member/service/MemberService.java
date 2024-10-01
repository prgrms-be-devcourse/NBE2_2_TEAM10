package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.service.ApplyService;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.exception.MemberException;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.token.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {                              /**로그인 로직*/
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FeedService feedService;
    private final ApplyService applyService;
    private JWTUtil jwtUtil;

    public MemberDTO read(String email, String password) {
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw MemberException.BAD_CREDENTIALS.get();
        }
        return entityToDto(member);
    }

    public MemberDTO read(String email) {                 /**단순 조회 로직*/
        Optional<Member> foundMember = memberRepository.findByEmail(email);
        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);

        return entityToDto(member);
    }

    public MemberDTO register(MemberDTO memberDTO) {      /**가입*/
        try{
            Member member = dtoToEntity(memberDTO);
            member = memberRepository.save(member);
            return entityToDto(member);
        } catch (Exception e){
            log.error("===="+e.getMessage());
            throw MemberException.NOT_REGISTER.get();
        }
    }

    public MemberDTO modify(MemberDTO memberDTO) {          /** 수정 */
        Optional<Member> foundMember = memberRepository.findByEmail(memberDTO.getEmail());
        Member member = foundMember.orElseThrow(MemberException.NOT_FOUND::get);

        try{
            member.changeMemberName(memberDTO.getMemberName());
            member.changeEmail(memberDTO.getEmail());
            member.changePassword(passwordEncoder.encode(memberDTO.getPassword()));
            member.changeLocation(memberDTO.getLocation());
            member.changeProfilePicture(memberDTO.getProfilePicture());
            member.changeRole(memberDTO.getRole());

            return entityToDto(member);
        } catch (Exception e){
            log.error("===="+e.getMessage());
            throw MemberException.NOT_MODIFIED.get();
        }
    }

    public void deleteMember(String token) {        /**  탈퇴(회원만) */
        try {// JWT 검증 및 클레임 추출
            Map<String, Object> claims = jwtUtil.validateToken(token);

            // 이메일 추출 (클레임에서)
            String email = (String) claims.get("email");

            // 이메일로 사용자를 검색합니다.
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            // 사용자 삭제
            memberRepository.delete(member);
        } catch (Exception e){
            log.error("===="+e.getMessage());
            throw MemberException.REMOVE_FAILED.get();
        }
    }

    private MemberDTO entityToDto(Member member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .password(member.getPassword())
                .email(member.getEmail())
                .location(member.getLocation())
                .role(member.getRole())
                .profilePicture(member.getProfilePicture())
                .createdAt(member.getCreatedAt())
                .feeds(feedService.readAll(member))
                .postApplications(applyService.readAll(member))
                .build();
    }

    private Member dtoToEntity(MemberDTO memberDTO) {
        return Member.builder()
                .memberId(memberDTO.getMemberId())
                .memberName(memberDTO.getMemberName())
                .password(memberDTO.getPassword())
                .email(memberDTO.getEmail())
                .location(memberDTO.getLocation())
                .role(memberDTO.getRole())
                .profilePicture(memberDTO.getProfilePicture())
                .createdAt(memberDTO.getCreatedAt())
                .build();
    }
}
