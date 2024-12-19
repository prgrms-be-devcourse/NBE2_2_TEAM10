package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberRequestDTO;
import com.prgrms2.java.bitta.member.dto.MemberResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    void join(MemberRequestDTO.Join joinDTO);
    MemberResponseDTO read(String username);
    void changePassword(String username, MemberRequestDTO.ChangePassword memberDTO);
    void update(String username, MemberRequestDTO.Modify memberDTO);
    void update(String username, MemberRequestDTO.Modify memberDTO, MultipartFile profileImage);
    void delete(String username);
}