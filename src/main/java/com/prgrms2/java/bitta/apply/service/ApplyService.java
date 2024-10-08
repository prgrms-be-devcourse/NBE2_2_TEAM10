package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.member.entity.Member;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ApplyService {
    List<ApplyDTO> readAll(Member member);

    Map<String, Object> register(ApplyDTO applyDTO);

    void delete(Long id);

    ApplyDTO read(Long id);

    ApplyDTO readByIdAndMember(Long id, Member member);

    List<ApplyDTO> getApplyForJobPost(Long jobPostId);
}
