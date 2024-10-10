package com.prgrms2.java.bitta.scout.service;

import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.service.MemberProvider;
import com.prgrms2.java.bitta.scout.dto.ScoutDTO;
import com.prgrms2.java.bitta.scout.entity.ScoutRequest;
import com.prgrms2.java.bitta.scout.repository.ScoutRequestRepository;
import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoutRequestService {
    private final ScoutRequestRepository scoutRequestRepository;
    private final FeedProvider feedProvider;
    private final MemberProvider memberProvider;

    @Transactional
    public ScoutDTO sendScoutRequest(ScoutDTO scoutDTO) {
        ScoutRequest request = dtoToEntity(scoutDTO);

        request = scoutRequestRepository.save(request);

        return entityToDto(request);
    }


    @Transactional(readOnly = true)
    public Page<ScoutDTO> getSentScoutRequests(Long senderId, Pageable pageable) {
        return scoutRequestRepository.findBySenderIdOrderById(senderId, pageable)
                .map(this::entityToDto);
    }


    @Transactional(readOnly = true)
    public Page<ScoutDTO> getReceivedScoutRequests(Long receiverId, Pageable pageable) {
        return scoutRequestRepository.findByReceiverIdOrderById(receiverId, pageable)
                .map(this::entityToDto);
    }

    private ScoutDTO entityToDto(ScoutRequest request) {
        return ScoutDTO.builder()
                .id(request.getId())
                .feedId(request.getFeed().getId())
                .senderId(request.getSender().getId())
                .receiverId(request.getReceiver().getId())
                .description(request.getDescription())
                .sentAt(request.getSentAt())
                .build();
    }

    private ScoutRequest dtoToEntity(ScoutDTO scoutDTO) {
        Feed feed = feedProvider.getById(scoutDTO.getFeedId());
        Member sender = memberProvider.getById(scoutDTO.getSenderId());
        Member receiver = memberProvider.getById(scoutDTO.getReceiverId());

        return ScoutRequest.builder()
                .id(scoutDTO.getId())
                .feed(feed)
                .sender(sender)
                .receiver(receiver)
                .description(scoutDTO.getDescription())
                .sentAt(scoutDTO.getSentAt())
                .build();
    }
}