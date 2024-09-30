package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;

    public JobPostServiceImpl(JobPostRepository jobPostRepository, MemberRepository memberRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    @Override
    public List<JobPostDTO> getList() {
        List<JobPost> jobPost = jobPostRepository.findAll();
        return jobPost.stream()
                .map(JobPostDTO::new)
                .collect(Collectors.toList());
    }

//    @Override
//    public JobPostDTO register(JobPostDTO jobPostDTO) {
//        JobPost jobPost = jobPostDTO.toEntity();
//        jobPost = jobPostRepository.save(jobPost);
//        return new JobPostDTO(jobPost);
//    }
    @Override
    public JobPostDTO register(JobPostDTO jobPostDTO) {
        try {
            JobPost jobPost = jobPostDTO.toEntity();
            jobPost = jobPostRepository.save(jobPost);
            return new JobPostDTO(jobPost);
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

            return new JobPostDTO(jobPost);

        } catch (Exception e){
            log.error(e.getMessage());
            throw JobPostException.NOT_MODIFIED.get();
        }
    }

    @Override
    public void remove(Long id) {
        Optional<JobPost> deleteJobPost = jobPostRepository.findById(id);
        JobPost jobPost = deleteJobPost.orElseThrow(JobPostException.NOT_FOUND::get);
        try {
            jobPostRepository.delete(jobPost);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw JobPostException.NOT_REMOVED.get();
        }
    }
}

