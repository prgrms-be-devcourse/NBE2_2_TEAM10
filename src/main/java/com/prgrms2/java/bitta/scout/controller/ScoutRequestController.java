package com.prgrms2.java.bitta.scout.controller;

import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/scout")
@RequiredArgsConstructor
public class ScoutRequestController {
    private final ScoutRequestService scoutRequestService;

    @PostMapping
    public ResponseEntity<?> sendScoutRequest(@RequestParam Long feedId, @RequestParam Long senderId, @RequestParam String description) {
        ScoutRequestDTO scoutRequest = scoutRequestService.sendScoutRequest(feedId, senderId, description);
        return ResponseEntity.ok().body(scoutRequest);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllScoutRequests(@PathVariable Long userId, Pageable pageable) {
        Page<ScoutRequestDTO> sentRequests = scoutRequestService.getSentScoutRequests(userId, pageable);
        Page<ScoutRequestDTO> receivedRequests = scoutRequestService.getReceivedScoutRequests(userId, pageable);

        return ResponseEntity.ok().body(Map.of(
                "sentRequests", sentRequests,
                "receivedRequests", receivedRequests
        ));
    }
}