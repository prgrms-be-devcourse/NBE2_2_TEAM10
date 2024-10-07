package com.prgrms2.java.bitta.scout.repository;

import com.prgrms2.java.bitta.scout.entity.ScoutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoutRequestRepository extends JpaRepository<ScoutRequest, Long> {
    Page<ScoutRequest> findBySenderId(Long senderId, Pageable pageable);
    Page<ScoutRequest> findByReceiverId(Long receiverId, Pageable pageable);
}