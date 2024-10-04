package com.prgrms2.java.bitta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.jobpost.controller.JobPostController;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
import com.prgrms2.java.bitta.jobpost.service.JobPostService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(JobPostController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
public class JobPostControllerTests {

    @MockBean
    private JobPostService jobPostService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JobPostDTO jobPostDTO;
    private List<JobPostDTO> jobPostDTOList;

    @BeforeEach
    void initialize() {
        jobPostDTO = JobPostDTO.builder()
                .id(1L)
                .memberId(1L)
                .title("Title")
                .description("Description")
                .location(Location.SEOUL)
                .payStatus(PayStatus.FREE)
                .updateAt(LocalDateTime.now().withNano(0))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .isClosed(false)
                .build();

        jobPostDTOList = new ArrayList<>();

        IntStream.rangeClosed(1, 5).forEach(i ->
            jobPostDTOList.add(JobPostDTO.builder()
                    .id((long) i)
                    .memberId(1L)
                    .title("Title" + i)
                    .description("Description" + i)
                    .location(Location.SEOUL)
                    .payStatus(PayStatus.FREE)
                    .updateAt(LocalDateTime.now().withNano(0))
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(7))
                    .isClosed(false)
                    .build()));
    }

    private ResultMatcher[] generateResultMatchers(JobPostDTO jobPostDTO) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();
        String prefix = "$.";

        resultMatchers.add(jsonPath(prefix + "id").value(jobPostDTO.getId()));
        resultMatchers.add(jsonPath(prefix + "memberId").value(jobPostDTO.getMemberId()));
        resultMatchers.add(jsonPath(prefix + "title").value(jobPostDTO.getTitle()));
        resultMatchers.add(jsonPath(prefix + "description").value(jobPostDTO.getDescription()));
        resultMatchers.add(jsonPath(prefix + "location").value(jobPostDTO.getLocation().toString()));
        resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDTO.getPayStatus().toString()));
        resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDTO.getUpdateAt().withNano(0).toString()));
        resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDTO.getStartDate().toString()));
        resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDTO.getEndDate().toString()));
        resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDTO.isClosed()));

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] generateResultMatchers(List<JobPostDTO> jobPostDTOList) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();

        for (int i = 0; i < jobPostDTOList.size(); i++) {
            JobPostDTO jobPostDTO = jobPostDTOList.get(i);
            String prefix = String.format("$.result.[%d].", i);

            resultMatchers.add(jsonPath(prefix + "id").value(jobPostDTO.getId()));
            resultMatchers.add(jsonPath(prefix + "title").value(jobPostDTO.getTitle()));
            resultMatchers.add(jsonPath(prefix + "discription").value(jobPostDTO.getDescription()));
            resultMatchers.add(jsonPath(prefix + "location").value(jobPostDTO.getLocation().toString()));
            resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDTO.getPayStatus().toString()));
            resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDTO.getUpdateAt()));
            resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDTO.getStartDate()));
            resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDTO.getEndDate()));
            resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDTO.isClosed()));
        }

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    @Test
    @DisplayName("전체 일거리 조회 (성공)")
    void findAll_JobPostsExist_ReturnList() throws Exception {
        // 페이징된 데이터 설정
        Page<JobPostDTO> jobPostPage = new PageImpl<>(jobPostDTOList);

        given(jobPostService.getList(any(PageRequestDTO.class))).willReturn(jobPostPage);

        mockMvc.perform(get("/api/v1/job-post")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(jobPostDTOList.size()));
    }

    @Test
    @DisplayName("단일 일거리 조회 (성공)")
    void readJobPost_JobPostExists_ReturnPost() throws Exception {
        given(jobPostService.read(anyLong())).willReturn(jobPostDTO);

        mockMvc.perform(get("/api/v1/job-post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDTO));
    }

    @Test
    @DisplayName("단일 일거리 조회 (실패) :: 검색 결과 없음")
    void readJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
        given(jobPostService.read(anyLong())).willThrow(JobPostException.NOT_FOUND.get());

        mockMvc.perform(get("/api/v1/job-post/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("게시글을 찾을 수 없습니다"));
    }

    @Test
    @DisplayName("일거리 등록 (성공)")
    void registerJobPost_PostNotDuplicated_ReturnResultDto() throws Exception {
        given(jobPostService.register(any(JobPostDTO.class))).willReturn(jobPostDTO);

        String content = objectMapper.writeValueAsString(jobPostDTO);

        mockMvc.perform(post("/api/v1/job-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDTO));
    }

    @Test
    @DisplayName("JobPost 등록 (실패) :: 유효성 검증 실패로 BadRequest 반환")
    void createJobPost_DtoValidationFails_HttpStatusBadRequest() throws Exception {
        // 필수 값들을 유효하지 않게 설정하여 유효성 검증 실패 유도
        JobPostDTO invalidJobPostDTO = new JobPostDTO(
                null,
                1L,
                null,
                " ",
                Location.BUSAN,
                PayStatus.PAID,
                LocalDateTime.now(),
                LocalDate.of(2024,10,2),
                LocalDate.of(2024,10,3),
                true
        );

        mockMvc.perform(post("/api/v1/job-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidJobPostDTO))  // 유효하지 않은 DTO 전송
                        .with(csrf()))
                .andDo(print())  // 요청 및 응답 출력
                .andExpect(status().isBadRequest());

    }



    @Test
    @DisplayName("일거리 수정 (성공)")
    void modifyJobPost_PostExists_ReturnResultDto() throws Exception {
        given(jobPostService.modify(any(JobPostDTO.class))).willReturn(jobPostDTO);

        String content = objectMapper.writeValueAsString(jobPostDTO);

        mockMvc.perform(put("/api/v1/job-post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDTO));
    }

    @Test
    @DisplayName("일거리 수정 (실패) :: 검색 결과 없음")
    void modifyJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
        given(jobPostService.modify(any(JobPostDTO.class))).willThrow(JobPostException.NOT_FOUND.get());

        String content = objectMapper.writeValueAsString(jobPostDTO);

        mockMvc.perform(put("/api/v1/job-post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("게시글을 찾을 수 없습니다"));
    }

    @Test // 수정 예정
    @DisplayName("일거리 삭제 (성공)")
    void deleteJobPost_PostExists_ReturnSuccessMessage() throws Exception {
        doNothing().when(jobPostService).remove(anyLong());

        mockMvc.perform(delete("/api/v1/job-post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("삭제가 완료되었습니다"));
    }

    @Test
    @DisplayName("일거리 삭제 (실패) :: 검색 결과 없음")
    void deleteJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
        doThrow(JobPostException.NOT_REMOVED.get()).when(jobPostService).remove(anyLong());

        mockMvc.perform(delete("/api/v1/job-post/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())  // NOT_REMOVED 예외가 400이라면
                .andExpect(jsonPath("$.error").value("게시글을 삭제할 수 없습니다"));
    }

}

