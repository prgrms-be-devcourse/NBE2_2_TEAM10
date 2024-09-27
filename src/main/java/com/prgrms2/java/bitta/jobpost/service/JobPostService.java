package com.prgrms2.java.bitta.jobpost.service;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import org.springframework.stereotype.Service;

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

