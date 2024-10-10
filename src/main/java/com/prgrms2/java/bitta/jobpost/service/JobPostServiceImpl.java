package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import com.prgrms2.java.bitta.apply.service.ApplyService;
import com.prgrms2.java.bitta.apply.util.ApplyProvider;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.media.service.MediaService;
import com.prgrms2.java.bitta.member.service.MemberProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final MemberProvider memberProvider;
    private final ApplyProvider applyProvider;
    private final MediaService mediaService;
    private final ApplyService applyService;
    private final ApplyRepository applyRepository;

    @Override
    @Transactional
    public Page<JobPostDTO> getList(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        Page<JobPost> jobPosts = jobPostRepository.getList(pageable);

        return jobPosts.map(this::entityToDto);
    }

    @Override
    @Transactional
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
    @Transactional
    public JobPostDTO read(Long jobPostId) {
        Optional<JobPost> jobPost = jobPostRepository.getJobPost(jobPostId);
        return jobPost.map(this::entityToDto).orElseThrow(JobPostException.NOT_REGISTERED::get);
    }

    @Override
    @Transactional
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

    @Transactional
    @Override
    public void removeJobPost(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(JobPostException.NOT_FOUND::get);
        List<Apply> applies = jobPost.getApply();
        if (applies != null && !applies.isEmpty()) {
            applies.forEach(apply -> apply.setJobPost(null)); applyRepository.deleteAllInBatch(applies);
        }
         if (jobPost.getMedia() != null) {
             mediaService.delete(jobPost.getMedia()); jobPost.setMedia(null);
         }
        jobPostRepository.delete(jobPost);
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
                .member(memberProvider.getById(jobPostDTO.getMemberId()))
                .apply(applyProvider.getAllByJobPost(jobPostDTO.getId()))
                .build();
    }
}

