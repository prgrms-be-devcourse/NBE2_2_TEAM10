package com.prgrms2.java.bitta.apply.controller;

import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
import com.prgrms2.java.bitta.apply.service.ApplyService;
import com.prgrms2.java.bitta.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/apply")
public class ApplyController {
    private final ApplyService applyService;

    @GetMapping
    public ResponseEntity<List<ApplyDTO>> findAll(Member member) {
        return ResponseEntity.ok(applyService.readAll(member));
    }

    @PostMapping
    public ResponseEntity<ApplyDTO> registerApply(@RequestBody ApplyDTO applyDTO) {
        return ResponseEntity.ok(applyService.register(applyDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplyDTO> readApply(@PathVariable("id") Long id) {
        return ResponseEntity.ok(applyService.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteApply(@PathVariable("id") Long id) {
        applyService.delete(id);
        return ResponseEntity.ok(Map.of("message", "delete complete"));
    }
}
