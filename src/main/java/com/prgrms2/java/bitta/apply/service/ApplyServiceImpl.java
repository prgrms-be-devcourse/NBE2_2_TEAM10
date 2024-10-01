package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.util.MemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {
    private final ApplyRepository applyRepository;
    private final MemberProvider memberProvider;
    private final JobPostProvider jobPostProvider;

    @Override
    public List<ApplyDTO> readAll(Member member) {
        List<Apply> applies = applyRepository.findAllByMember(member);

        if (applies.isEmpty()) {
            return null;
        }

        return applies.stream().map(this::entityToDto).toList();
    }

    private Apply dtoToEntity(ApplyDTO applyDTO) {
        return Apply.builder()
                .applicationId(applyDTO.getApplicationId())
                .member(memberProvider.getById(applyDTO.getMemberId()))
                .jobPost(jobPostProvider.getById(applyDTO.getJobPostId()))
                .appliedAt(applyDTO.getAppliedAt())
                .build();
    }

    private ApplyDTO entityToDto(Apply apply) {
        return ApplyDTO.builder()
                .applicationId(apply.getApplicationId())
                .memberId(apply.getMember().getMemberId())
                .jobPostId(apply.getJobPost().getId())
                .appliedAt(apply.getAppliedAt())
                .build();
    }
}
