package com.prgrms2.java.bitta.scout.controller;

import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scout")
@RequiredArgsConstructor
public class ScoutRequestController {
    private final ScoutRequestService scoutRequestService;

    @PostMapping("/send")
    public ResponseEntity<?> sendScoutRequest(@RequestParam Long feedId, @RequestParam Long senderId, @RequestParam String description) {
        ScoutRequestDTO scoutRequest = scoutRequestService.sendScoutRequest(feedId, senderId, description);
        return ResponseEntity.ok().body(scoutRequest);
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<ScoutRequestDTO>> getSentScoutRequests(@PathVariable Long senderId) {
        List<ScoutRequestDTO> sentRequests = scoutRequestService.getSentScoutRequests(senderId);
        return ResponseEntity.ok(sentRequests);
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<ScoutRequestDTO>> getReceivedScoutRequests(@PathVariable Long receiverId) {
        List<ScoutRequestDTO> receivedRequests = scoutRequestService.getReceivedScoutRequests(receiverId);
        return ResponseEntity.ok(receivedRequests);
    }
}