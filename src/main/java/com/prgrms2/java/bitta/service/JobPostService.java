package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.dto.JobPostDTO;

import java.util.List;

public interface JobPostService {
    static JobPostDTO register(JobPostDTO jobPostDTO) {
        return null;
    }

    JobPostDTO read(Long jobPostId);

    JobPostDTO modify(JobPostDTO jobPostDTO);

    void remove(Long jobPostId);

    List<JobPostDTO> getList(JobPostDTO jobPostDTO);
}

