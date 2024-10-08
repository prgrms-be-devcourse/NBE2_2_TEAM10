package com.prgrms2.java.bitta.apply.util;

import com.prgrms2.java.bitta.apply.entity.Apply;
import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplyProvider {
    private final ApplyRepository applyRepository;

    public List<Apply> getAllByJobPost(Long jobPostId) {
        List<Apply> applies = applyRepository.findAllByJobPostId(jobPostId);

        if (applies.isEmpty()) {
            return null;
        }

        return applies;
    }
}
