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
//
//    @Override
//    public TokenResponseDto validate(MemberRequestDto.Login loginDto) {
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
//
//        Authentication authentication = authenticationManagerBuilder
//                .getObject().authenticate(authenticationToken);
//
//        return tokenProvider.generate(authentication);
//    }

//    @Override
//    @Transactional
//    public void insert(MemberRequestDto.Register registerDto) {
//        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        try {
//            memberRepository.save(memberMapper.dtoToEntity(registerDto));
//        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
//            throw MemberException.NOT_REGISTER.get();
//        }
//    }
//
//    @Override
//    @Transactional
//    public void insert(MemberRequestDto.Register registerDto, MultipartFile multipartFile) {
//        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        try {
//            Member member = memberRepository.save(memberMapper.dtoToEntity(registerDto));
//
//            mediaService.upload(multipartFile, member.getId(), null);
//        } catch (DataIntegrityViolationException | ConstraintViolationException | MediaTaskException e) {
//            throw MemberException.NOT_REGISTER.get();
//        }
//    }

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
    public void changePassword(MemberRequestDto.ChangePassword memberDto) {
        Member member = memberRepository.findById(memberDto.getId())
                .orElseThrow(MemberException.NOT_FOUND::get);

        if (passwordEncoder.matches(memberDto.getBeforePassword(), member.getPassword())) {
            member.setPassword(passwordEncoder.encode(memberDto.getAfterPassword()));
        }

        throw MemberException.BAD_CREDENTIALS.get();
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
