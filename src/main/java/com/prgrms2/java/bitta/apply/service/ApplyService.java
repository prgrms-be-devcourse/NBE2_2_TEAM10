package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.member.entity.Member;

import java.util.List;

public interface ApplyService {
    List<ApplyDTO> readAll(Member member);
}
