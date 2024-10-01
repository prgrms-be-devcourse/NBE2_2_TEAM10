package com.prgrms2.java.bitta.member.service;


import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.security.JwtToken;

public interface MemberService {
    JwtToken signIn(String username, String password);
    MemberDTO signUp(SignUpDTO signUpDTO);
}
