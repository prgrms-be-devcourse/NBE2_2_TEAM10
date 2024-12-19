package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.media.exception.MediaTaskException;
import com.prgrms2.java.bitta.media.service.MediaService;
import com.prgrms2.java.bitta.member.dto.MemberRequestDTO;
import com.prgrms2.java.bitta.member.dto.MemberResponseDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.exception.MemberException;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MediaService mediaService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void join(MemberRequestDTO.Join joinDTO) {
        if (memberRepository.existsByUsername(joinDTO.getUsername())) {
            throw MemberException.DUPLICATE.get();
        }

        Member member = Member.builder()
                .username(joinDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .nickname(joinDTO.getNickname())
                .address(joinDTO.getAddress())
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);
    }

    @Override
    public MemberResponseDTO read(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        MemberResponseDTO responseDTO = new MemberResponseDTO(member);

        if (member.getMedia() != null) {
            responseDTO.setProfileUrl(mediaService.getUrl(member.getMedia()));
        }

        return responseDTO;
    }

    @Override
    @Transactional
    public void changePassword(String username, MemberRequestDTO.ChangePassword changePasswordDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        if (!bCryptPasswordEncoder.matches(changePasswordDTO.getBeforePassword(), member.getPassword())) {
            throw MemberException.BAD_CREDENTIALS.get();
        }

        member.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getAfterPassword()));
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void update(String username, MemberRequestDTO.Modify memberDTO) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        member.setNickname(memberDTO.getNickname());
        member.setAddress(memberDTO.getAddress());

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw MemberException.NOT_MODIFIED.get();
        }
    }

    @Override
    @Transactional
    public void update(String username, MemberRequestDTO.Modify memberDTO, MultipartFile profileImage) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        if (member.getMedia() != null) {
            mediaService.deleteExistFile(member.getMedia());
        }

        mediaService.upload(profileImage, member.getId(), null);
        member.setNickname(memberDTO.getNickname());
        member.setAddress(memberDTO.getAddress());

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException | ConstraintViolationException | MediaTaskException e) {
            throw MemberException.NOT_MODIFIED.get();
        }
    }
    @Override
    @Transactional
    public void delete(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        try {
            memberRepository.delete(member);
            if (member.getMedia() != null) {
                mediaService.deleteExistFile(member.getMedia());
            }
            log.info("회원 삭제 완료 - Username: {}", username);
        } catch (Exception e) {
            log.error("회원 삭제 실패 - Username: {}", username, e);
            throw MemberException.REMOVE_FAILED.get();
        }
    }
}