package com.prgrms2.java.bitta.controller;

import com.prgrms2.java.bitta.scout.controller.ScoutRequestController;
import com.prgrms2.java.bitta.scout.dto.ScoutRequestDTO;
import com.prgrms2.java.bitta.scout.service.ScoutRequestService;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScoutRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ScoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoutRequestService scoutRequestService;

    private ScoutRequestDTO scoutRequestDTO;
    private FeedDTO feedDTO;

    @BeforeEach
    public void setUp() {

        feedDTO = FeedDTO.builder()
                .id(1L)
                .title("Test Feed")
                .content("Sample Content")
                .memberId(1L)
                .createdAt(LocalDateTime.now())
                .medias(List.of())
                .build();


        scoutRequestDTO = ScoutRequestDTO.builder()
                .id(1L)
                .feed(feedDTO)
                .senderId(1L)
                .receiverId(2L)
                .description("Test Description")
                .sentAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void sendScoutRequest_ShouldReturn200() throws Exception {

        when(scoutRequestService.sendScoutRequest(anyLong(), anyLong(), any(String.class)))
                .thenReturn(scoutRequestDTO);

        mockMvc.perform(post("/api/v1/scout/send")
                        .param("feedId", "1")
                        .param("senderId", "1")
                        .param("description", "Test Description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.feed.id").value(1L))
                .andExpect(jsonPath("$.senderId").value(1L))
                .andExpect(jsonPath("$.receiverId").value(2L))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    public void getAllScoutRequests_ShouldReturn200() throws Exception {

        Page<ScoutRequestDTO> scoutRequestPage = new PageImpl<>(List.of(scoutRequestDTO));
        when(scoutRequestService.getSentScoutRequests(anyLong(), any(Pageable.class)))
                .thenReturn(scoutRequestPage);
        when(scoutRequestService.getReceivedScoutRequests(anyLong(), any(Pageable.class)))
                .thenReturn(scoutRequestPage);

        mockMvc.perform(get("/api/v1/scout/all/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentRequests.content[0].id").value(1L))
                .andExpect(jsonPath("$.receivedRequests.content[0].id").value(1L));
    }
}