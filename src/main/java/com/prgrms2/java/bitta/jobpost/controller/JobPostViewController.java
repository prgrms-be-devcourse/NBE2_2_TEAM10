package com.prgrms2.java.bitta.jobpost.controller;

import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
import com.prgrms2.java.bitta.jobpost.service.JobPostService;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobPostViewController {

    private final JobPostService jobPostService;
    private final JobPostRepository jobPostRepository;
    private final MemberRepository memberRepository;

    @GetMapping("jobpost")
    public String getjobpost(Model model, PageRequestDTO pageRequestDTO) {
        List<JobPost> result = jobPostRepository.findAll();
        model.addAttribute("jobPosts", result);

        return "jobpost/jobpost.html";
    }
}
