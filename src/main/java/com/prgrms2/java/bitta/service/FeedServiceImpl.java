package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.dto.FeedDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedServiceImpl implements FeedService {
    @Override
    public Optional<FeedDto> read(Long id) {
        return null;
    }

    @Override
    public List<FeedDto> readAll() {
        return null;
    }

    @Override
    public String insert(FeedDto feedDto) {
        return null;
    }

    @Override
    public Optional<FeedDto> update(FeedDto feedDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
