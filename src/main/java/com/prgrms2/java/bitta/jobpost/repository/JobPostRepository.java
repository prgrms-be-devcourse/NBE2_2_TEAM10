package com.prgrms2.java.bitta.jobpost.repository;

import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.entity.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query("SELECT j FROM JobPost j WHERE j.id = :id")
    Optional<JobPostDTO> getJobPostDTO(@Param("id") Long id);

    @Query("DELETE FROM JobPost j WHERE j.id = :id")
    Long deleteByIdAndReturnCount(Long id);

    @Query("SELECT j FROM JobPost j ORDER BY 'id' DESC ")
    Page<JobPostDTO> getList(Pageable pageable);

    @Query("SELECT j FROM JobPost j WHERE j.member.id = :memberId")
    Page<JobPostDTO> findJobPostByMember(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT j FROM JobPost j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<JobPostDTO> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
