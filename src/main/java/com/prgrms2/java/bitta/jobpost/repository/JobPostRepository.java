package com.prgrms2.java.bitta.jobpost.repository;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query("SELECT j FROM JobPost j WHERE j.id = :id")
    Optional<JobPostDTO> getJobPostDTO(@Param("id") Long id);
}
