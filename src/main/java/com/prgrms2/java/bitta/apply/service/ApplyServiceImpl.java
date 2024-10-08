package com.prgrms2.java.bitta.apply.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.exception.ApplyException;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.jobpost.util.JobPostProvider;
import com.prgrms2.java.bitta.member.dto.MemberProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {
    private final ApplyRepository applyRepository;
    private final JobPostRepository jobPostRepository;
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
    public Map<String, Object> register(ApplyDTO applyDTO) {
        try {
            Apply apply = dtoToEntity(applyDTO);
            apply = applyRepository.save(apply);

            Map<String, Object> response = new HashMap<>();
            response.put("message", apply.getMember().getNickname() + "님 지원 완료");
            response.put("data", entityToDto(apply));

            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ApplyException.NOT_REGISTERED.get();
        }
    }

    @Override
    public void delete(Long id) {
        if (applyRepository.deleteByIdAndReturnCount(id) == 0) {
            throw ApplyException.NOT_REMOVED.get();
        }
    }

    @Override
    public ApplyDTO read(Long id) {
        Optional<ApplyDTO> applyDTO = applyRepository.getApplyDTO(id);
        return applyDTO.orElseThrow(ApplyException.NOT_FOUND::get);
    }

    // 게시글 작성자의 ID 값 가져오는 메서드
    @Override
    public ApplyDTO readByIdAndMember(Long id, Member member) {
        Optional<ApplyDTO> applyDTO = applyRepository.findByIdAndMember(id, member);
        return applyDTO.orElseThrow(ApplyException.NOT_FOUND::get);
    }

    @Override
    public List<ApplyDTO> getApplyForJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow();

        List<ApplyDTO> apply = applyRepository.findAllByJobPost(jobPost);

        return apply;
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
