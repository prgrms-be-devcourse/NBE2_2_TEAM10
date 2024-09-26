package com.prgrms2.java.bitta.jobpost.repository;

import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

}
