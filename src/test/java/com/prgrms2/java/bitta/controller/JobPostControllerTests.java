//package com.prgrms2.java.bitta.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms2.java.bitta.jobpost.controller.JobPostController;
//import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
//import com.prgrms2.java.bitta.jobpost.entity.Location;
//import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
//import com.prgrms2.java.bitta.jobpost.service.JobPostService;
//import com.prgrms2.java.bitta.token.util.JWTUtil;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(JobPostController.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters = false)
//public class JobPostControllerTests {
//    @MockBean
//    private JobPostService jobPostService;
//
//    @MockBean
//    private JWTUtil jwtUtil;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private JobPostDTO jobPostDto;
//
//    private List<JobPostDTO> jobPostDtoList;
//
//    @BeforeEach
//    void initialize() {
//        jobPostDto = JobPostDTO.builder()
//                .id(1L)
//                .userId(1L)
//                .title("Title")
//                .description("Description")
//                .location(Location.SEOUL)
//                .payStatus(PayStatus.FREE)
//                .updateAt(LocalDateTime.now().withNano(0))
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.now().plusDays(7))
//                .isClosed(false)
//                .build();
//
//        jobPostDtoList = new ArrayList<>();
//
//        IntStream.rangeClosed(1, 5).forEach(i -> {
//            JobPostDTO _jobPostDto = JobPostDTO.builder()
//                    .id((long) i)
//                    .userId(1L)
//                    .title("Title" + i)
//                    .description("Description" + i)
//                    .location(Location.SEOUL)
//                    .payStatus(PayStatus.FREE)
//                    .updateAt(LocalDateTime.now().withNano(0))
//                    .startDate(LocalDate.now())
//                    .endDate(LocalDate.now().plusDays(7))
//                    .isClosed(false)
//                    .build();
//
//            jobPostDtoList.add(_jobPostDto);
//        });
//    }
//
//    private ResultMatcher[] generateResultMatchers(JobPostDTO jobPostDto) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//        String prefix = "$.";
//
//        resultMatchers.add(jsonPath(prefix + "id").value(jobPostDto.getId()));
//        resultMatchers.add(jsonPath(prefix + "userId").value(jobPostDto.getUserId()));
//        resultMatchers.add(jsonPath(prefix + "title").value(jobPostDto.getTitle()));
//        resultMatchers.add(jsonPath(prefix + "description").value(jobPostDto.getDescription()));
//        resultMatchers.add(jsonPath(prefix + "location").value(jobPostDto.getLocation().toString()));
//        resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDto.getPayStatus().toString()));
//        resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDto.getUpdateAt().withNano(0).toString()));
//        resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDto.getStartDate().toString()));
//        resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDto.getEndDate().toString()));
//        resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDto.isClosed()));
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    private ResultMatcher[] generateResultMatchers(List<JobPostDTO> jobPostDtoList) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//
//        for (int i = 0; i < jobPostDtoList.size(); i++) {
//            JobPostDTO jobPostDto = jobPostDtoList.get(i);
//            String prefix = String.format("$[%d].", i);
//
//            resultMatchers.add(jsonPath(prefix + "id").value(jobPostDto.getId()));
//            resultMatchers.add(jsonPath(prefix + "userId").value(jobPostDto.getUserId()));
//            resultMatchers.add(jsonPath(prefix + "title").value(jobPostDto.getTitle()));
//            resultMatchers.add(jsonPath(prefix + "description").value(jobPostDto.getDescription()));
//            resultMatchers.add(jsonPath(prefix + "location").value(jobPostDto.getLocation().toString()));
//            resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDto.getPayStatus().toString()));
//            resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDto.getUpdateAt().withNano(0).toString()));
//            resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDto.getStartDate().toString()));
//            resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDto.getEndDate().toString()));
//            resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDto.isClosed()));
//        }
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    @Test
//    @DisplayName("포스트 전체 조회 (성공)")
//    void findAll_PostExists_ReturnList() throws Exception {
//        given(jobPostService.getList()).willReturn(jobPostDtoList);
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(get("/api/v1/jobPost")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDtoList));
//    }
//
//    @Test
//    @DisplayName("포스트 전체 조회 (실패) :: 검색 결과 없음")
//    void findAll_PostNotExists_HttpStatusNotFound() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 전체 조회 (실패) :: DTO 검증 실패")
//    void findAll_DtoNotValid_ThrowException() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 단일 조회 (성공)")
//    void readJobPost_JobPostExists_ReturnPost() throws Exception {
//        given(jobPostService.read(anyLong()))
//                .willReturn(jobPostDto);
//
//        mockMvc.perform(get("/api/v1/jobPost/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("포스트 단일 조회 (실패) :: 검색 결과 없음")
//    void readJobPost_PostNotExists_HttpStatusNotFound() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 단일 조회 (실패) :: DTO 검증 실패")
//    void readJobPost_DtoNotValid_ThrowsException() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 등록 (성공)")
//    void createJobPost_PostNotDuplicated_ReturnResultDto() throws Exception {
//        given(jobPostService.register(any(JobPostDTO.class)))
//                .willReturn(jobPostDto);
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(post("/api/v1/jobPost")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("포스트 등록 (실패) :: DTO 검증 실패")
//    void createJobPost_DtoNotValid_HttpBadRequest() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 수정 (성공)")
//    void updateJobPost_PostExists_ReturnResultDto() throws Exception {
//        given(jobPostService.modify(any(JobPostDTO.class)))
//                .willReturn(jobPostDto);
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(put("/api/v1/jobPost/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("포스트 수정 (실패) :: 검색 결과 없음")
//    void updateJobPost_PostNotExists_HttpStatusNotFound() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 수정 (실패) :: DTO 검증 실패")
//    void updateJobPost_DtoNotValid_HttpStatusBadRequest() {
//
//    }
//
//    @Test
//    @DisplayName("포스트 삭제 (성공)")
//    void deleteJobPost_PostExists_ReturnSuccessMessage() throws Exception {
//        doNothing().when(jobPostService).remove(anyLong());
//
//        mockMvc.perform(delete("/api/v1/jobPost/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("success"));
//    }
//
//    @Test
//    @DisplayName("포스트 삭제 (실패) :: 검색 결과 없음")
//    void deleteJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//
//    }
//}
