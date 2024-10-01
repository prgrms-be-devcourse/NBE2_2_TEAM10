package com.prgrms2.java.bitta.jobpost.util;

import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobPostProvider {
    private final JobPostRepository jobPostRepository;

    public JobPost getById(Long id) {
        return jobPostRepository.findById(id).orElse(null);
    }
}
