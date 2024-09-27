package com.prgrms2.java.bitta.feed.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.repository.FeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;

    // Constructor for dependency injection
    public FeedServiceImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedDTO> read(Long id) {
        return feedRepository.findById(id).map(this::entityToDto);  // Ensure consistent method reference
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedDTO> readAll() {
        return feedRepository.findAll().stream()
                .map(this::entityToDto)  // Consistent method reference
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String insert(FeedDTO feedDTO) {
        Feed feed = dtoToEntity(feedDTO);
        feedRepository.save(feed);
        return "Feed created successfully";
    }

    @Override
    @Transactional
    public Optional<FeedDTO> update(FeedDTO feedDTO) {
        Optional<Feed> feedOpt = feedRepository.findById(feedDTO.getFeedId());

        if (feedOpt.isPresent()) {
            Feed feed = feedOpt.get();
            feed.setTitle(feedDTO.getTitle());
            feed.setContent(feedDTO.getContent());
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