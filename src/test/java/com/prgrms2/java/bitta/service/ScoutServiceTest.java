package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.feed.entity.Feed;
import com.prgrms2.java.bitta.feed.service.FeedProvider;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.dto.MemberProvider;
import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.entity.ScoutRequest;
import com.prgrms2.java.bitta.scout.repository.ScoutRequestRepository;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ScoutServiceTest {

    @Mock
    private ScoutRequestRepository scoutRequestRepository;

    @Mock
    private FeedProvider feedProvider; // Mock FeedProvider

    @Mock
    private MemberProvider memberProvider; // Mock MemberProvider

    @InjectMocks
    private ScoutRequestService scoutRequestService;

    private Member sender;
    private Member receiver;
    private Feed feed;
    private ScoutRequest scoutRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        sender = Member.builder().id(1L).username("sender").build();
        receiver = Member.builder().id(2L).username("receiver").build();
        feed = Feed.builder().id(1L).title("Sample Feed").member(receiver).build();

        scoutRequest = ScoutRequest.builder()
                .id(1L)
                .feed(feed)
                .sender(sender)
                .receiver(receiver)
                .description("Scout Request Description")
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void sendScoutRequest_shouldReturnScoutRequestDTO() {

        when(feedProvider.getById(1L)).thenReturn(feed);
        when(memberProvider.getById(1L)).thenReturn(sender);

        when(scoutRequestRepository.save(any(ScoutRequest.class))).thenReturn(scoutRequest);

        ScoutRequestDTO result = scoutRequestService.sendScoutRequest(1L, 1L, "Scout Request Description");

        assertEquals(1L, result.getFeed().getId());
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getReceiverId());
    }

    @Test
    public void getSentScoutRequests_shouldReturnPaginatedResults() {
        Page<ScoutRequest> scoutRequestPage = new PageImpl<>(List.of(scoutRequest));
        when(scoutRequestRepository.findBySenderId(any(Long.class), any(PageRequest.class))).thenReturn(scoutRequestPage);

        Page<ScoutRequestDTO> result = scoutRequestService.getSentScoutRequests(1L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getSenderId());
    }

    @Test
    public void getReceivedScoutRequests_shouldReturnPaginatedResults() {
        Page<ScoutRequest> scoutRequestPage = new PageImpl<>(List.of(scoutRequest));
        when(scoutRequestRepository.findByReceiverId(any(Long.class), any(PageRequest.class))).thenReturn(scoutRequestPage);

        Page<ScoutRequestDTO> result = scoutRequestService.getReceivedScoutRequests(2L, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(2L, result.getContent().get(0).getReceiverId());
    }
}