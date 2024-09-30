package com.prgrms2.java.bitta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.jobpost.controller.JobPostController;
import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
import com.prgrms2.java.bitta.jobpost.service.JobPostService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobPostControllerTests {
    @MockBean
    private JobPostService jobPostService;

    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static JobPostDTO jobPostDto;

    private static List<JobPostDTO> jobPostDtoList;

    @BeforeAll
    static void initialize() {
        jobPostDto = new JobPostDTO();

        jobPostDto.setJobPostId(1L);
        jobPostDto.setUserId(1L);
        jobPostDto.setTitle("Title");
        jobPostDto.setDescription("Description");
        jobPostDto.setLocation(Location.SEOUL);
        jobPostDto.setPayStatus(PayStatus.FREE);
        jobPostDto.setUpdateAt(LocalDateTime.now());
        jobPostDto.setStartDate(LocalDate.now());
        jobPostDto.setEndDate(LocalDate.now().plusDays(7));
        jobPostDto.setClosed(false);

        jobPostDtoList = new ArrayList<>();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            JobPostDTO _jobPostDto = new JobPostDTO();

            _jobPostDto.setJobPostId((long) i);
            _jobPostDto.setUserId(1L);
            _jobPostDto.setTitle("Title" + i);
            _jobPostDto.setDescription("Description" + i);
            _jobPostDto.setLocation(Location.SEOUL);
            _jobPostDto.setPayStatus(PayStatus.FREE);
            _jobPostDto.setUpdateAt(LocalDateTime.now());
            _jobPostDto.setStartDate(LocalDate.now());
            _jobPostDto.setEndDate(LocalDate.now().plusDays(7));
            _jobPostDto.setClosed(false);

            jobPostDtoList.add(_jobPostDto);
        });
    }

    private ResultMatcher[] generateResultMatchers(JobPostDTO jobPostDto) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();
        String prefix = "$.";

        resultMatchers.add(jsonPath(prefix + "jobPostId").value(jobPostDto.getJobPostId()));
        resultMatchers.add(jsonPath(prefix + "userId").value(jobPostDto.getUserId()));
        resultMatchers.add(jsonPath(prefix + "title").value(jobPostDto.getTitle()));
        resultMatchers.add(jsonPath(prefix + "description").value(jobPostDto.getDescription()));
        resultMatchers.add(jsonPath(prefix + "location").value(jobPostDto.getLocation()));
        resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDto.getPayStatus()));
        resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDto.getUpdateAt()));
        resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDto.getStartDate()));
        resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDto.getEndDate()));
        resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDto.isClosed()));

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    private ResultMatcher[] generateResultMatchers(List<JobPostDTO> jobPostDtoList) {
        List<ResultMatcher> resultMatchers = new ArrayList<>();

        for (int i = 0; i < jobPostDtoList.size(); i++) {
            JobPostDTO jobPostDto = new JobPostDTO();
            String prefix = String.format("$[%d].", i);

            resultMatchers.add(jsonPath(prefix + "jobPostId").value(jobPostDto.getJobPostId()));
            resultMatchers.add(jsonPath(prefix + "userId").value(jobPostDto.getUserId()));
            resultMatchers.add(jsonPath(prefix + "title").value(jobPostDto.getTitle()));
            resultMatchers.add(jsonPath(prefix + "description").value(jobPostDto.getDescription()));
            resultMatchers.add(jsonPath(prefix + "location").value(jobPostDto.getLocation()));
            resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDto.getPayStatus()));
            resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDto.getUpdateAt()));
            resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDto.getStartDate()));
            resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDto.getEndDate()));
            resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDto.isClosed()));
        }

        return resultMatchers.toArray(ResultMatcher[]::new);
    }

    @Test
    @DisplayName("포스트 전체 조회 (성공)")
    void findAll_PostExists_ReturnList() throws Exception {
        given(jobPostService.getList(any(JobPostDTO.class)))
                .willReturn(jobPostDtoList);

        String content = objectMapper.writeValueAsString(jobPostDto);

        mockMvc.perform(get("/api/v1/jobPost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDtoList));
    }

    @Test
    @DisplayName("포스트 전체 조회 (실패) :: 검색 결과 없음")
    void findAll_PostNotExists_HttpStatusNotFound() {

    }

    @Test
    @DisplayName("포스트 전체 조회 (실패) :: DTO 검증 실패")
    void findAll_DtoNotValid_ThrowException() {

    }

    @Test
    @DisplayName("포스트 단일 조회 (성공)")
    void readJobPost_JobPostExists_ReturnPost() throws Exception {
        given(jobPostService.read(anyLong()))
                .willReturn(jobPostDto);

        mockMvc.perform(get("/api/v1/jobPost/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDto));
    }

    @Test
    @DisplayName("포스트 단일 조회 (실패) :: 검색 결과 없음")
    void readJobPost_PostNotExists_HttpStatusNotFound() {

    }

    @Test
    @DisplayName("포스트 단일 조회 (실패) :: DTO 검증 실패")
    void readJobPost_DtoNotValid_ThrowsException() {

    }

    @Test
    @DisplayName("포스트 등록 (성공)")
    void createJobPost_PostNotDuplicated_ReturnResultDto() throws Exception {
        given(JobPostService.register(any(JobPostDTO.class)))
                .willReturn(jobPostDto);

        String content = objectMapper.writeValueAsString(jobPostDto);

        mockMvc.perform(post("/api/v1/jobPost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDto));
    }

    @Test
    @DisplayName("포스트 등록 (실패) :: DTO 검증 실패")
    void createJobPost_DtoNotValid_HttpBadRequest() {

    }

    @Test
    @DisplayName("포스트 수정 (성공)")
    void updateJobPost_PostExists_ReturnResultDto() throws Exception {
        given(jobPostService.modify(any(JobPostDTO.class)))
                .willReturn(jobPostDto);

        String content = objectMapper.writeValueAsString(jobPostDto);

        mockMvc.perform(put("/api/v1/jobPost/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(generateResultMatchers(jobPostDto));
    }

    @Test
    @DisplayName("포스트 수정 (실패) :: 검색 결과 없음")
    void updateJobPost_PostNotExists_HttpStatusNotFound() {

    }

    @Test
    @DisplayName("포스트 수정 (실패) :: DTO 검증 실패")
    void updateJobPost_DtoNotValid_HttpStatusBadRequest() {

    }

    @Test
    @DisplayName("포스트 삭제 (성공)")
    void deleteJobPost_PostExists_ReturnSuccessMessage() throws Exception {
        doNothing().when(jobPostService).remove(anyLong());

        mockMvc.perform(delete("/api/v1/jobPost/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("포스트 삭제 (실패) :: 검색 결과 없음")
    void deleteJobPost_PostNotExists_HttpStatusNotFound() throws Exception {

    }
}
