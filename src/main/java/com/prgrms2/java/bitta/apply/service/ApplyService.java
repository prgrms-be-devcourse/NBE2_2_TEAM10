package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.member.entity.Member;
import java.util.List;
import java.util.Map;

public interface ApplyService {
    List<ApplyDTO> readAll(Member member);

    Map<String, Object> register(ApplyDTO applyDTO);

    void delete(Long id);

    void delete(List<Apply> apply);

    ApplyDTO read(Long id);

    List<ApplyDTO> getApplyForJobPost(Long jobPostId);

    ApplyDTO findById(Long id);
}
