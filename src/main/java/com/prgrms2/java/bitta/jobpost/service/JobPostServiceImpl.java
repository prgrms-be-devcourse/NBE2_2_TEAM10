package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.apply.service.ApplyServiceImpl;
import com.prgrms2.java.bitta.apply.util.ApplyProvider;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.member.dto.MemberProvider;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final MemberProvider memberProvider;
    private final ApplyProvider applyProvider;
    private final MemberRepository memberRepository;
    private final ApplyRepository applyRepository;
    private final ApplyServiceImpl applyServiceImpl;

    @Override
    public Page<JobPostDTO> getList(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        return jobPostRepository.getList(pageable);
    }

    @Override
    public JobPostDTO register(JobPostDTO jobPostDTO) {
        try {
            JobPost jobPost = dtoToEntity(jobPostDTO);
            jobPost = jobPostRepository.save(jobPost);
            return entityToDto(jobPost);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw JobPostException.NOT_REGISTERED.get();
        }
    }

    @Override
    public JobPostDTO read(Long jobPostId) {
        Optional<JobPostDTO> jobPostDTO = jobPostRepository.getJobPostDTO(jobPostId);
        return jobPostDTO.orElseThrow(JobPostException.NOT_FOUND::get);
    }

    @Override
    public JobPostDTO modify(JobPostDTO jobPostDTO) {
        Optional<JobPost> modifyJobPost = jobPostRepository.findById(jobPostDTO.getId());
        JobPost jobPost = modifyJobPost.orElseThrow(JobPostException.NOT_FOUND::get);

        try {
            jobPost.setTitle(jobPostDTO.getTitle());
            jobPost.setDescription(jobPostDTO.getDescription());
            jobPost.setLocation(jobPostDTO.getLocation());
            jobPost.setPayStatus(jobPostDTO.getPayStatus());
            jobPost.setShootMethod(jobPostDTO.getShootMethod());

            jobPostRepository.save(jobPost);

            return entityToDto(jobPost);
        } catch (Exception e){
            log.error(e.getMessage());
            throw JobPostException.NOT_MODIFIED.get();
        }
    }


    @Override
    public void remove(Long id) {
        if (jobPostRepository.deleteByIdAndReturnCount(id) == 0) {
            throw JobPostException.NOT_REMOVED.get();
        }
    }

    @Override   // 게시자의 다른 글 검색
    public Page<JobPostDTO> getJobPostByMember(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());

        return jobPostRepository.findJobPostByMember(memberId, pageable);
    }

    @Override   // 특정 키워드를 포함한 게시물 검색
    public Page<JobPostDTO> searchJobPosts(String keyword, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        return jobPostRepository.searchByKeyword(keyword, pageable);
    }

    private JobPostDTO entityToDto(JobPost jobPost) {
        return JobPostDTO.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .location(jobPost.getLocation())
                .payStatus(jobPost.getPayStatus())
                .isClosed(jobPost.isClosed())
                .shootMethod(jobPost.getShootMethod())
                .auditionDate(jobPost.getAuditionDate())
                .startDate(jobPost.getStartDate())
                .endDate(jobPost.getEndDate())
                .updateAt(jobPost.getUpdatedAt())
                .memberId(jobPost.getMember().getId())
                .build();
    }

    private JobPost dtoToEntity(JobPostDTO jobPostDTO) {
        return JobPost.builder()
                .id(jobPostDTO.getId())
                .title(jobPostDTO.getTitle())
                .description(jobPostDTO.getDescription())
                .location(jobPostDTO.getLocation())
                .payStatus(jobPostDTO.getPayStatus())
                .isClosed(jobPostDTO.isClosed())
                .shootMethod(jobPostDTO.getShootMethod())
                .auditionDate(jobPostDTO.getAuditionDate())
                .startDate(jobPostDTO.getStartDate())
                .endDate(jobPostDTO.getEndDate())
                .updatedAt(jobPostDTO.getUpdateAt())
                .member(memberProvider.getById(jobPostDTO.getId()))
                .apply(applyProvider.getAllByJobPost(jobPostDTO.getId()))
                .build();
    }
}

