package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.MemberRequestDto;
import com.prgrms2.java.bitta.member.dto.MemberResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
//    TokenResponseDto validate(MemberRequestDto.Login loginDto);

    MemberResponseDto.Information read(Long id);

//    void insert(MemberRequestDto.Register registerDto);

//    void insert(MemberRequestDto.Register registerDto, MultipartFile multipartFile);

    void changePassword(MemberRequestDto.ChangePassword memberDto);

    void update(MemberRequestDto.Modify memberDto);

    void update(MemberRequestDto.Modify memberDto, MultipartFile profileImage);

    void delete(Long id);

    boolean checkAuthority(Long id, String username);
}
