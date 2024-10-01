package com.prgrms2.java.bitta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.feed.controller.FeedController;
import com.prgrms2.java.bitta.feed.dto.FeedDTO;
import com.prgrms2.java.bitta.feed.exception.FeedException;
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
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    private FeedDTO feedDTO;

    private List<FeedDTO> feedDTOList;

    @BeforeAll
    void initialize() {
        feedDTO = FeedDTO.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .createdAt(LocalDateTime.now())
                .email("member@email.com")
                .build();

        feedDTOList = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> feedDTOList.add(FeedDTO.builder()
                .id((long) i)
                .title("Title" + i)
                .content("Content" + i)
                .createdAt(LocalDateTime.now())
                .email("member@email.com")
                .build()));
    }

    private ResultMatcher[] generateResultMatchers(FeedDTO feedDTO) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();
        String prefix = "$.result.";

        resultMatchers.add(jsonPath(prefix + "id").value(feedDTO.getId()));
        resultMatchers.add(jsonPath(prefix + "title").value(feedDTO.getTitle()));
        resultMatchers.add(jsonPath(prefix + "content").value(feedDTO.getContent()));
        resultMatchers.add(jsonPath(prefix + "email").value(feedDTO.getEmail()));
        resultMatchers.add(jsonPath(prefix + "createdAt").value(feedDTO.getCreatedAt()));

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] generateResultMatchers(List<FeedDTO> feedDTOList) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();

        for (int i = 0; i < feedDTOList.size(); i++) {
            FeedDTO feedDTO = feedDTOList.get(i);
            String prefix = String.format("$result.[%d].", i);

            resultMatchers.add(jsonPath(prefix + "id").value(feedDTO.getId()));
            resultMatchers.add(jsonPath(prefix + "title").value(feedDTO.getTitle()));
            resultMatchers.add(jsonPath(prefix + "content").value(feedDTO.getContent()));
            resultMatchers.add(jsonPath(prefix + "email").value(feedDTO.getEmail()));
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
                .andExpect(jsonPath("$.message")
                        .value("피드를 성공적으로 조회했습니다."))
                .andExpectAll(generateResultMatchers(feedDTOList));
    }

    @Test
    @DisplayName("피드 전체 조회 (실패) :: 검색 결과 없음")
    void getFeed_FeedNotExists_NotFound() throws Exception {
        given(feedService.readAll())
                .willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/feed"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("피드가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("피드 단일 조회 (성공)")
    void getFeedById_FeedExists_ReturnFeed() throws Exception {
        given(feedService.read(anyLong()))
                .willReturn(feedDTO);

        mockMvc.perform(get("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("피드를 성공적으로 조회했습니다."))
                .andExpectAll(generateResultMatchers(feedDTO));
    }

    @Test
    @DisplayName("피드 단일 조회 (실패) :: 검색 결과 없음")
    void getFeedById_FeedNotExists_HttpStatusNotFound() throws Exception {
        given(feedService.read(anyLong()))
                .willThrow(FeedException.CANNOT_FOUND.get());

        mockMvc.perform(get("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("피드가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("피드 등록 (성공)")
    void createFeed_FeedNotDuplicated_ReturnMessage() throws Exception {
        doNothing().when(feedService).insert(any(FeedDTO.class));

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(post("/api/v1/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("피드가 등록되었습니다."));
    }

    @Test
    @DisplayName("피드 등록 (실패) :: ID 값이 존재함")
    void createFeed_IdExistsInDto_HttpStatusBadRequest() throws Exception {
        doThrow(FeedException.BAD_REQUEST.get())
                .when(feedService).insert(any(FeedDTO.class));

        String content = objectMapper.writeValueAsString(FeedDTO.builder().build());

        mockMvc.perform(post("/api/v1/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("잘못된 요청입니다."));
    }

    @Test
    @DisplayName("피드 등록 (실패) :: DTO 검증 실패")
    void createFeed_WrongRequestDto_ThrowMethodArgumentNotValidException() throws Exception {
        FeedDTO emptyDto = FeedDTO.builder().build();

        String content = objectMapper.writeValueAsString(emptyDto);

        mockMvc.perform(put("/api/v1/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.reason")
                        .value("제목은 비워둘 수 없습니다."));
    }

    @Test
    @DisplayName("피드 수정 (성공)")
    void modifyFeed_FeedDtoIsNotEmpty_ReturnFeedDto() throws Exception {
        doNothing().when(feedService).update(any(FeedDTO.class), photos, videos);

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(put("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("피드가 수정되었습니다."));
    }

    @Test
    @DisplayName("피드 수정 (실패) :: 검색 결과 없음")
    void modifyFeed_FeedDtoIsNotEmpty_HttpStatusBadRequest() throws Exception {
        doThrow(FeedException.CANNOT_FOUND.get())
                .when(feedService).update(any(FeedDTO.class), photos, videos);

        String content = objectMapper.writeValueAsString(feedDTO);

        mockMvc.perform(put("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("피드가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("피드 수정 (실패) :: DTO 검증 실패")
    void modifyFeed_WrongRequestDto_ThrowMethodArgumentNotValidException() throws Exception {
        FeedDTO emptyDto = FeedDTO.builder().build();

        String content = objectMapper.writeValueAsString(emptyDto);

        mockMvc.perform(put("/api/v1/feed/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.reason")
                        .value("제목은 비워둘 수 없습니다."));
    }

    @Test
    @DisplayName("피드 삭제 (성공)")
    void deleteFeed_FeedExists_HttpStatusNoContent() throws Exception {
        doNothing().when(feedService).delete(anyLong());

        mockMvc.perform(delete("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("피드가 삭제되었습니다."));
    }

    @Test
    @DisplayName("피드 삭제 (실패) :: 검색 결과 없음")
    void deleteFeed_FeedNotExists_HttpStatusNotFound() throws Exception {
        doThrow(FeedException.CANNOT_DELETE.get())
                .when(feedService).delete(anyLong());

        mockMvc.perform(delete("/api/v1/feed/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("삭제할 피드가 존재하지 않습니다"));
    }

    @Test
    @DisplayName("피드 삭제 (실패) :: 잘못된 경로 변수")
    void deleteFeed_WrongPathVariable_ThrowConstraintViolationException() throws Exception {
        mockMvc.perform(delete("/api/v1/feed/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.reason")
                        .value("ID는 음수가 될 수 없습니다."));
    }
}
