package com.prgrms2.java.bitta.scout.controller;

import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ScoutRequestDTO>> getSentScoutRequests(@PathVariable Long senderId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScoutRequestDTO> sentRequests = scoutRequestService.getSentScoutRequests(senderId, pageable);
        return ResponseEntity.ok(sentRequests);
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<Page<ScoutRequestDTO>> getReceivedScoutRequests(@PathVariable Long receiverId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScoutRequestDTO> receivedRequests = scoutRequestService.getReceivedScoutRequests(receiverId, pageable);
        return ResponseEntity.ok(receivedRequests);
    }
}