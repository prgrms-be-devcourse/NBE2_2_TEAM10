package com.prgrms2.java.bitta.scout.controller;

import com.prgrms2.java.bitta.scout.dto.ScoutDTO;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<?> sendScoutRequest(@RequestBody ScoutDTO scoutDTO) {
        scoutDTO = scoutRequestService.sendScoutRequest(scoutDTO);
        return ResponseEntity.ok().body(Map.of("message", "스카우트를 저장했습니다.", "result", scoutDTO));
    }

    @GetMapping("/sender/{memberId}")
    public ResponseEntity<?> getSendScoutRequests(@PathVariable Long memberId
            , @RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "10", value = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ScoutDTO> sentRequests = scoutRequestService.getSentScoutRequests(memberId, pageable);

        return ResponseEntity.ok().body(Map.of(
                "message", "보낸 스카우트를 조회했습니다.",
                "receivedRequests", sentRequests
        ));
    }

    @GetMapping("/receiver/{memberId}")
    public ResponseEntity<?> getReceiveScoutRequests(@PathVariable Long memberId
            , @RequestParam(required = false, defaultValue = "0", value = "page") int page
            , @RequestParam(required = false, defaultValue = "10", value = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ScoutDTO> receivedRequests = scoutRequestService.getReceivedScoutRequests(memberId, pageable);

        return ResponseEntity.ok().body(Map.of(
                "message", "받은 스카우트를 조회했습니다.",
                "receivedRequests", receivedRequests
        ));
    }
}