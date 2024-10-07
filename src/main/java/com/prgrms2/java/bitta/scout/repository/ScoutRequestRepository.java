package com.prgrms2.java.bitta.scout.repository;

import com.prgrms2.java.bitta.scout.entity.ScoutRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScoutRequestRepository extends JpaRepository<ScoutRequest, Long> {
    List<ScoutRequest> findBySenderId(Long senderId);  // Find all requests sent by a specific user.
    List<ScoutRequest> findByReceiverId(Long receiverId);  // Find all requests received by a specific user.
}