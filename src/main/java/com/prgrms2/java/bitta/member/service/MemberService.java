package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.security.JwtToken;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {
    JwtToken signIn(String username, String password);
    MemberDTO signUp(SignUpDTO signUpDTO);

    MemberDTO getMemberById(Long id);
    MemberDTO updateMember(Long id, MemberDTO memberDTO, MultipartFile profileImage, boolean removeProfileImage) throws IOException;
    void deleteMember(Long id);
}
