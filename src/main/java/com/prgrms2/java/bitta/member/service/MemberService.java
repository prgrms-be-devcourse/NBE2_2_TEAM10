package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    void join(MemberRequestDto.Join joinDTO);    //Join 기능 병합

    MemberResponseDto.Information read(Long id);

    void changePassword(MemberRequestDto.ChangePassword memberDto);

    void update(MemberRequestDto.Modify memberDto);

    void update(MemberRequestDto.Modify memberDto, MultipartFile profileImage);

    void delete(Long id);

    boolean checkAuthority(Long id, String username);
}
