package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.media.exception.MediaTaskException;
import com.prgrms2.java.bitta.media.service.MediaService;
import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.exception.MemberException;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.member.util.MemberMapper;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MediaService mediaService;
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void join(MemberRequestDto.Join joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String nickname = joinDTO.getNickname();
        String address = joinDTO.getAddress();

        Boolean isExist = memberRepository.existsByUsername(username);

        if (isExist) {
            return;
        }

        Member data = new Member();
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(nickname);
        data.setAddress(address);
        data.setRole("ROLE_USER");

        memberRepository.save(data);
    }

    @Override
    public MemberResponseDto.Information read(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberException.NOT_FOUND::get);

        MemberResponseDto.Information memberDto = memberMapper
                .entityToDto(member);

        if (member.getMedia() != null) {
            memberDto.setProfileUrl(mediaService.getUrl(member.getMedia()));
        }

        return memberDto;
    }

    @Override
    public void changePassword(MemberRequestDto.ChangePassword changePasswordDTO) {
        Long memberId = changePasswordDTO.getId();
        String beforePassword = changePasswordDTO.getBeforePassword();
        String afterPassword = changePasswordDTO.getAfterPassword();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.NOT_FOUND::get);

        if (bCryptPasswordEncoder.matches(beforePassword, member.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(afterPassword));
            memberRepository.save(member);
        } else {
            throw MemberException.BAD_CREDENTIALS.get();
        }
    }

    @Override
    @Transactional
    public void update(MemberRequestDto.Modify memberDto) {
        Member member = memberRepository.findById(memberDto.getId())
                .orElseThrow(MemberException.NOT_FOUND::get);

        member.setUsername(memberDto.getUsername());
        member.setNickname(memberDto.getNickname());
        member.setAddress(memberDto.getAddress());

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException | ConstraintViolationException | MediaTaskException e) {
            throw MemberException.NOT_MODIFIED.get();
        }
    }

    @Override
    @Transactional
    public void update(MemberRequestDto.Modify memberDto, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberDto.getId())
                .orElseThrow(MemberException.NOT_FOUND::get);

        if (member.getMedia() != null) {
            mediaService.deleteExistFile(member.getMedia());
        }

        mediaService.upload(profileImage, member.getId(), null);

        member.setUsername(memberDto.getUsername());
        member.setNickname(memberDto.getNickname());
        member.setAddress(memberDto.getAddress());

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException | ConstraintViolationException | MediaTaskException e) {
            throw MemberException.NOT_MODIFIED.get();
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberException.NOT_FOUND::get);

        try {
            memberRepository.deleteById(id);
            mediaService.deleteExistFile(member.getMedia());
            log.info("회원 삭제 완료 - ID: {}", id);
        } catch (Exception e) {
            log.error("회원 삭제 실패 - ID: {}", id, e);
            throw MemberException.REMOVE_FAILED.get();
        }
    }

    @Override
    public boolean checkAuthority(Long id, String username) {
        return memberRepository.existsByIdAndUsername(id, username);
    }
}
