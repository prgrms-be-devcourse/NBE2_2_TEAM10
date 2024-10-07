package com.prgrms2.java.bitta.scout.service;

import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.entity.ScoutRequest;
import com.prgrms2.java.bitta.scout.repository.ScoutRequestRepository;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoutRequestService {
    private final ScoutRequestRepository scoutRequestRepository;
    private final FeedProvider feedProvider;
    private final MemberProvider memberProvider;

    @Transactional
    public ScoutRequestDTO sendScoutRequest(Long feedId, Long senderId, String description) {
        Feed feed = feedProvider.getById(feedId);
        ScoutRequest request = ScoutRequest.builder()
                .feed(feed)
                .sender(memberProvider.getById(senderId))
                .receiver(feed.getMember())
                .description(description)
                .sentAt(LocalDateTime.now())
                .build();
        scoutRequestRepository.save(request);
        return entityToDto(request);
    }

    @Transactional(readOnly = true)
    public List<ScoutRequestDTO> getSentScoutRequests(Long senderId) {
        return scoutRequestRepository.findBySenderId(senderId).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScoutRequestDTO> getReceivedScoutRequests(Long receiverId) {
        return scoutRequestRepository.findByReceiverId(receiverId).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private ScoutRequestDTO entityToDto(ScoutRequest request) {
        return ScoutRequestDTO.builder()
                .id(request.getId())
                .feed(FeedDTO.builder()
                        .id(request.getFeed().getId())
                        .title(request.getFeed().getTitle())
                        .build())
                .senderId(request.getSender().getId())
                .receiverId(request.getReceiver().getId())
                .description(request.getDescription())
                .sentAt(request.getSentAt())
                .build();
    }
}