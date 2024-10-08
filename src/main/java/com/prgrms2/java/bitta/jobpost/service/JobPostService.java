package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobPostService {
    JobPostDTO register(JobPostDTO jobPostDTO);

    void remove(Long id);

    JobPostDTO read(Long id);

    JobPostDTO modify(JobPostDTO jobPostDTO);

    Page<JobPostDTO> getList(PageRequestDTO pageRequestDTO);

    Page<JobPostDTO> getJobPostByMember(Long memberId, PageRequestDTO pageRequestDTO);

    Page<JobPostDTO> searchJobPosts(String keyword, PageRequestDTO pageRequestDTO);

}

