package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.dto.JobPostDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostServiceImpl implements JobPostService {
    @Override
    public JobPostDTO read(Long jobPostId) {
        return null;
    }

    @Override
    public JobPostDTO modify(JobPostDTO jobPostDTO) {
        return null;
    }

    @Override
    public void remove(Long jobPostId) {

    }

    @Override
    public List<JobPostDTO> getList(JobPostDTO jobPostDTO) {
        return List.of();
    }

}
