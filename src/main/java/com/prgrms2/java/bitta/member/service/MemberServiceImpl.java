package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final String defaultProfileImg = "/images/default_avatar.png";

    @Value("${file.root.path}")
    private String fileRootPath;
    
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

        // USER 권한 부여
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        return MemberDTO.toDTO(memberRepository.save(signUpDTO.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshAccessToken(refreshToken);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        String profile = member.getProfile() != null ? member.getProfile() : defaultProfileImg;

        return new MemberDTO(member);
    }

    @Transactional
    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO, MultipartFile profileImage, boolean removeProfileImage) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (removeProfileImage || profileImage == null) {
            member.setProfile(defaultProfileImg);
        } else {
            String imagePath = saveProfileImage(profileImage);
            member.setProfile(imagePath);
        }

        member.setNickname(memberDTO.getNickname());
        member.setAddress(memberDTO.getAddress());

        return MemberDTO.toDTO(member);
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String directory = fileRootPath + "/uploads/profile_images/";
        Path uploadPath = Paths.get(directory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        profileImage.transferTo(filePath.toFile());

        return directory + fileName;
    }


    public void deleteProfileImage(String profileImg) {
        if (!profileImg.equals(defaultProfileImg)) {
            File file = new File(profileImg);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + id + "인 회원을 찾을 수 없습니다."));
        memberRepository.delete(member);
    }
}
