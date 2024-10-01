package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageService profileImageService;

    @Transactional
    @Override
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    @Override
    public MemberDTO signUp(SignUpDTO signUpDTO) {
        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        // 기본 프로필 이미지 설정
        String defaultProfileImg = profileImageService.getDefaultProfileImage();

        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER 권한 부여

        // 회원 가입 시 기본 프로필 이미지 추가
        Member newMember = signUpDTO.toEntity(encodedPassword, roles);
        newMember.setProfileImg(defaultProfileImg);

        return MemberDTO.toDTO(memberRepository.save(newMember));
        //return MemberDTO.toDTO(memberRepository.save(signUpDTO.toEntity(encodedPassword, roles)));
    }

    // 프로필 이미지 수정
    @Transactional
    public void updateProfileImage(Long id, MultipartFile file) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 파일 저장 후 경로 설정
        String profileImagePath = profileImageService.saveProfileImage(file);
        member.setProfileImg(profileImagePath);

        memberRepository.save(member);
    }

    // 프로필 이미지 삭제(기본 이미지로 재설정)
    @Transactional
    public void resetProfileImageToDefault(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setProfileImg(profileImageService.getDefaultProfileImage());

        memberRepository.save(member);
    }

}