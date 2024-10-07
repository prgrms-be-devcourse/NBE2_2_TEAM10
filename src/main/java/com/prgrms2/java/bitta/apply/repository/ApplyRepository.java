package com.prgrms2.java.bitta.apply.repository;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    @Query("SELECT a FROM Apply a WHERE a.member = :member")
    List<Apply> findAllByMember(@Param("member") Member member);

    @Query("SELECT a FROM Apply a WHERE a.jobPost.id = :jobPostId")
    List<Apply> findAllByJobPostId(@Param("jobPostId") Long jobPostId);

    @Query("SELECT a FROM Apply a WHERE a.id = :id")
    Optional<ApplyDTO> getApplyDTO(@Param("id") Long id);

    @Query("DELETE FROM Apply a WHERE a.id = :id")
    int deleteByIdAndReturnCount(Long id);

    Optional<Apply> findByIdAndMember(Long id, Member member);
}
