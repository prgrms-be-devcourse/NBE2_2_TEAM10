package com.prgrms2.java.bitta.controller;

import com.prgrms2.java.bitta.dto.JobPostDTO;
import com.prgrms2.java.bitta.service.JobPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobPost")
@Log4j2
public class JobPostController {
    private final JobPostService jobPostService;

    @GetMapping
    public ResponseEntity<List<JobPostDTO>> findAll(@Validated JobPostDTO jobPostDTO) {
        return ResponseEntity.ok(jobPostService.getList(jobPostDTO));
    }

    @PostMapping
    public ResponseEntity<JobPostDTO> createJobPost(@Validated @RequestBody JobPostDTO jobPostDTO) {
        return ResponseEntity.ok(JobPostService.register(jobPostDTO));
    }

    @GetMapping("/{jobPostId}")
    public ResponseEntity<JobPostDTO> readJobPost(@PathVariable("jobPostId") Long jobPostId) {
        return ResponseEntity.ok(jobPostService.read(jobPostId));
    }

    @PutMapping("/{jobPostId}")
    public ResponseEntity<JobPostDTO> updateJobPost(@Validated @RequestBody JobPostDTO jobPostDTO, @PathVariable("jobPostId") Long jobPostId) {
        return ResponseEntity.ok(jobPostService.modify(jobPostDTO));
    }

    @DeleteMapping("/{jobPostId}")
    public ResponseEntity<Map<String, String>> deleteJobPost(@PathVariable("jobPostId") Long jobPostId) {
        jobPostService.remove(jobPostId);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

}
