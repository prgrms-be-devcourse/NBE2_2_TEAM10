package com.prgrms2.java.bitta.jobpost.controller;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.service.JobPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobpost")
@Log4j2
public class JobPostController {
    private final JobPostService jobPostService;

    @GetMapping
    public ResponseEntity<List<JobPostDTO>> findAll() {
        return ResponseEntity.ok(jobPostService.getList());
    }

    @PostMapping
    public ResponseEntity<JobPostDTO> registerJobPost(@RequestBody JobPostDTO jobPostDTO) {
        return ResponseEntity.ok(jobPostService.register(jobPostDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostDTO> readJobPost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(jobPostService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostDTO> modifyJobPost(@RequestBody JobPostDTO jobPostDTO, @PathVariable("id") Long id) {
        return ResponseEntity.ok(jobPostService.modify(jobPostDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteJobPost(@PathVariable("id") Long id) {
        jobPostService.remove(id);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
