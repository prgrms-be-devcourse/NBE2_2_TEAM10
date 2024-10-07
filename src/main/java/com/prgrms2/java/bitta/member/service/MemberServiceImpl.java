package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.exception.MemberException;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.JwtTokenProvider;
import com.prgrms2.java.bitta.security.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageService profileImageService;

    @Transactional
    @Override
    public JwtToken signIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    @Override
    public MemberDTO signUp(SignUpDTO signUpDTO) {
        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            throw MemberException.DUPLICATE.get();
        }

        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());
        List<String> roles = List.of("USER");

        return MemberDTO.toDTO(memberRepository.save(signUpDTO.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken reissueToken(String accessToken, String refreshToken) {
        try {
            return jwtTokenProvider.reissueToken(accessToken, refreshToken);
        } catch (RuntimeException e) {
            log.warn("토큰 갱신 실패: {}", e.getMessage());
            throw new InvalidTokenException("토큰 갱신에 실패했습니다. 다시 로그인해주세요.");
        }
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.NOT_FOUND.get());

        String profile = member.getProfile() != null ? member.getProfile() : profileImageService.getDefaultProfileImgPath();
        member.setProfile(profileImageService.getThumbnailFile(profile).getPath());

        return MemberDTO.toDTO(member);
    }

    @Transactional
    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO, MultipartFile profileImage, boolean removeProfileImage) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.NOT_FOUND.get());

        boolean isUpdated = updateMemberProfile(member, profileImage, removeProfileImage);

        if (memberDTO.getNickname() != null && !memberDTO.getNickname().isBlank()) {
            member.setNickname(memberDTO.getNickname());
            isUpdated = true;
        }

        if (memberDTO.getAddress() != null && !memberDTO.getAddress().isBlank()) {
            member.setAddress(memberDTO.getAddress());
            isUpdated = true;
        }

        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isBlank()) {
            member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            isUpdated = true;
        }

        if (!isUpdated) {
            throw MemberException.NOT_MODIFIED.get();
        }

        memberRepository.save(member);
        return MemberDTO.toDTO(member);
    }

    private boolean updateMemberProfile(Member member, MultipartFile profileImage, boolean removeProfileImage) throws IOException {
        if (removeProfileImage) {
            profileImageService.deleteProfileImage(member.getProfile());
            member.setProfile(profileImageService.getDefaultProfileImgPath());
            return true;
        } else if (profileImage != null && !profileImage.isEmpty()) {
            profileImageService.deleteProfileImage(member.getProfile());
            String imagePath = profileImageService.saveProfileImage(profileImage);
            profileImageService.createThumbnail(imagePath);
            member.setProfile(imagePath);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.NOT_FOUND.get());
        try {
            profileImageService.deleteProfileImage(member.getProfile());
            memberRepository.delete(member);
            log.info("회원 삭제 완료 - ID: {}", id);
        } catch (Exception e) {
            log.error("회원 삭제 실패 - ID: {}", id, e);
            throw MemberException.REMOVE_FAILED.get();
        }
    }
}
