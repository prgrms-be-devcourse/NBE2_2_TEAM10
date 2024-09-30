package com.prgrms2.java.bitta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.feed.controller.FeedController;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.service.FeedService;
import com.prgrms2.java.bitta.token.util.JWTUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeedControllerTests {
    @MockBean
    private FeedService feedService;

    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static FeedDTO feedDTO;

    private static List<FeedDTO> feedDTOList;

    @BeforeAll
    static void initialize() {
        feedDTO = FeedDTO.builder()
                .feedId(1L)
                .title("Title")
                .content("Content")
                .createdAt(LocalDateTime.now())
                .memberId(1L)
                .build();

        feedDTOList = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> feedDTOList.add(FeedDTO.builder()
                .feedId((long) i)
                .title("Title" + i)
                .content("Content" + i)
                .createdAt(LocalDateTime.now())
                .memberId((long) i)
                .build()));
    }

    private ResultMatcher[] generateResultMatchers(FeedDTO feedDTO) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();
        String prefix = "$.";

        resultMatchers.add(jsonPath(prefix + "feedId").value(feedDTO.getFeedId()));
        resultMatchers.add(jsonPath(prefix + "title").value(feedDTO.getTitle()));
        resultMatchers.add(jsonPath(prefix + "content").value(feedDTO.getContent()));
        resultMatchers.add(jsonPath(prefix + "memberId").value(feedDTO.getMemberId()));
        resultMatchers.add(jsonPath(prefix + "createdAt").value(feedDTO.getCreatedAt()));

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] generateResultMatchers(List<FeedDTO> feedDTOList) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();

        for (int i = 0; i < feedDTOList.size(); i++) {
            FeedDTO feedDTO = feedDTOList.get(i);
            String prefix = String.format("$[%d].", i);

            resultMatchers.add(jsonPath(prefix + "feedId").value(feedDTO.getFeedId()));
            resultMatchers.add(jsonPath(prefix + "title").value(feedDTO.getTitle()));
            resultMatchers.add(jsonPath(prefix + "content").value(feedDTO.getContent()));
            resultMatchers.add(jsonPath(prefix + "memberId").value(feedDTO.getMemberId()));
            resultMatchers.add(jsonPath(prefix + "createdAt").value(feedDTO.getCreatedAt()));
        }

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    @Test
    @DisplayName("피드 전체 조회 (성공)")
    void getFeed_FeedExists_ReturnList() throws Exception {
        given(feedService.readAll())
                .willReturn(feedDTOList);

        mockMvc.perform(get("/api/v1/feed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(feedDTOList));
    }

    @Test
    @DisplayName("피드 전체 조회 (실패) :: 검색 결과 없음")
    void getFeed_FeedNotExists_NotFound() throws Exception {
        given(feedService.readAll())
                .willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/feed"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("피드 단일 조회 (성공)")
    void getFeedById_FeedExists_ReturnFeed() throws Exception {
        given(feedService.read(anyLong()))
                .willReturn(Optional.of(feedDTO));

        mockMvc.perform(get("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(feedDTO));
    }

    @Test
    @DisplayName("피드 단일 조회 (실패) :: 검색 결과 없음")
    void getFeedById_FeedNotExists_HttpStatusNotFound() throws Exception {
        given(feedService.read(anyLong()))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("피드 등록 (성공)")
    void createFeed_FeedNotDuplicated_ReturnMessage() throws Exception {
        given(feedService.insert(any(FeedDTO.class)))
                .willReturn("Result Message");

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(post("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Result Message"));
    }

    @Test
    @DisplayName("피드 등록 (실패) :: DTO 검증 실패")
    void createFeed_FeedDtoIsEmpty_HttpStatusNotFound() throws Exception {
        String content = objectMapper.writeValueAsString(FeedDTO.builder().build());

        mockMvc.perform(post("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("피드 수정 (성공)")
    void modifyFeed_FeedDtoIsNotEmpty_ReturnFeedDto() throws Exception {
        given(feedService.update(any(FeedDTO.class)))
                .willReturn(Optional.of(feedDTO));

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(put("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(feedDTO));
    }

    @Test
    @DisplayName("피드 수정 (실패) :: DTO 검증 실패")
    void modifyFeed_FeedDtoIsNotEmpty_HttpStatusBadRequest() throws Exception {
        given(feedService.update(any(FeedDTO.class)))
                .willReturn(Optional.empty());

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(put("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("피드 삭제 (성공)")
    void deleteFeed_FeedExists_HttpStatusNoContent() throws Exception {
        given(feedService.delete(anyLong()))
                .willReturn(true);

        mockMvc.perform(delete("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("피드 삭제 (실패) :: 검색 결과 없음")
    void deleteFeed_FeedNotExists_HttpStatusNotFound() throws Exception {
        given(feedService.delete(anyLong()))
                .willReturn(false);

        mockMvc.perform(delete("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
