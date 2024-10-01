package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.exception.ApplyException;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.member.dto.MemberProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Override
    public ApplyDTO register(ApplyDTO applyDTO) {
        try {
            Apply apply = dtoToEntity(applyDTO);
            apply = applyRepository.save(apply);
            return entityToDto(apply);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ApplyException.NOT_REGISTERED.get();
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Apply> deleteApply = applyRepository.findById(id);
        Apply apply = deleteApply.orElseThrow(ApplyException.NOT_FOUND::get);

        try {
            applyRepository.delete(apply);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ApplyException.NOT_REMOVED.get();
        }
    }

    @Override
    public ApplyDTO read(Long id) {
        Optional<ApplyDTO> applyDTO = applyRepository.getApplyDTO(id);
        return applyDTO.orElseThrow(ApplyException.NOT_FOUND::get);
    }

    private Apply dtoToEntity(ApplyDTO applyDTO) {
        return Apply.builder()
                .id(applyDTO.getId())
                .member(memberProvider.getById(applyDTO.getMemberId()))
                .jobPost(jobPostProvider.getById(applyDTO.getJobPostId()))
                .appliedAt(applyDTO.getAppliedAt())
                .build();
    }

    private ApplyDTO entityToDto(Apply apply) {
        return ApplyDTO.builder()
                .id(apply.getId())
                .memberId(apply.getMember().getId())
                .jobPostId(apply.getJobPost().getId())
                .appliedAt(apply.getAppliedAt())
                .build();
    }
}
