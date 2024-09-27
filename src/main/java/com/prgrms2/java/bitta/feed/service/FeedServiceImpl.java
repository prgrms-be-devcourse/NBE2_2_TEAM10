package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDto;
import com.prgrms2.java.bitta.feed.entity.Feed;
import org.springframework.stereotype.Service;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;

    public FeedServiceImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public Optional<FeedDto> read(Long id) {
        return feedRepository.findById(id).map(this::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedDto> readAll() {
        return feedRepository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String insert(FeedDto feedDto) {
        Feed feed = dtoToEntity(feedDto);
        feedRepository.save(feed);
        return "피드 생성";
    }

    @Override
    @Transactional
    public Optional<FeedDto> update(FeedDto feedDto) {
        Optional<Feed> feedOpt = feedRepository.findById(feedDto.getFeedId());

        if (feedOpt.isPresent()) {
            Feed feed = feedOpt.get();
            feed.setTitle(feedDto.getTitle());
            feed.setContent(feedDto.getContent());
            feedRepository.save(feed);
            return Optional.of(entityToDto(feed));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (feedRepository.existsById(id)) {
            feedRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
