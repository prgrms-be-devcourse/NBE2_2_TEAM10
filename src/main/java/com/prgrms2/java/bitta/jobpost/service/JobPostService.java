package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface JobPostService {
    JobPostDTO register(JobPostDTO jobPostDTO);

    JobPostDTO read(Long id);

    JobPostDTO modify(JobPostDTO jobPostDTO);

    Page<JobPostDTO> getList(PageRequestDTO pageRequestDTO);

    void removeJobPost(Long jobPostId);

}

