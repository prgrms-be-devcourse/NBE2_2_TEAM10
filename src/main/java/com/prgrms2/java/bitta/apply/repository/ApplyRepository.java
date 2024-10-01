package com.prgrms2.java.bitta.apply.repository;

import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    @Query("SELECT a FROM Apply a WHERE a.member = :member")
    List<Apply> findAllByMember(@Param("member") Member member);

    @Query("SELECT a FROM Apply a WHERE a.jobPost.id = :jobPostId")
    List<Apply> findAllByJobPostId(@Param("jobPostId") Long jobPostId);
}
