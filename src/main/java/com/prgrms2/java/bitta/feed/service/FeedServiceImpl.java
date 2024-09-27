package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedServiceImpl implements FeedService {
    @Override
    public Optional<FeedDTO> read(Long id) {
        return null;
    }

    @Override
    public List<FeedDTO> readAll() {
        return null;
    }

    @Override
    public String insert(FeedDTO feedDto) {
        return null;
    }

    @Override
    public Optional<FeedDTO> update(FeedDTO feedDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
