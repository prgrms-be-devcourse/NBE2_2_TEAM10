package com.prgrms2.java.bitta.member.repository;

import com.prgrms2.java.bitta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsById(Long id);

    boolean existsByIdAndUsername(Long id, String username);
}
