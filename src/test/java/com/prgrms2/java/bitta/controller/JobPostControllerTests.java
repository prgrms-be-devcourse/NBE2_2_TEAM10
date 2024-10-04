//package com.prgrms2.java.bitta.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms2.java.bitta.jobpost.controller.JobPostController;
//import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
//import com.prgrms2.java.bitta.jobpost.dto.PageRequestDTO;
//import com.prgrms2.java.bitta.jobpost.entity.Location;
//import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
//import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
//import com.prgrms2.java.bitta.jobpost.service.JobPostService;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(JobPostController.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters = false)
//public class JobPostControllerTests {
//
//    @MockBean
//    private JobPostService jobPostService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private JobPostDTO jobPostDto;
//    private List<JobPostDTO> jobPostDtoList;
//
//    @BeforeEach
//    void initialize() {
//        jobPostDto = JobPostDTO.builder()
//                .id(1L)
//                .memberId(1L)
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
//                    .memberId(1L)
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
//        return new ResultMatcher[]{
//                jsonPath("$.id").value(jobPostDto.getId()),
//                jsonPath("$.userId").value(jobPostDto.getMemberId()),
//                jsonPath("$.title").value(jobPostDto.getTitle()),
//                jsonPath("$.description").value(jobPostDto.getDescription()),
//                jsonPath("$.location").value(jobPostDto.getLocation().toString()),
//                jsonPath("$.payStatus").value(jobPostDto.getPayStatus().toString()),
//                jsonPath("$.updateAt").value(jobPostDto.getUpdateAt().withNano(0).toString()),
//                jsonPath("$.startDate").value(jobPostDto.getStartDate().toString()),
//                jsonPath("$.endDate").value(jobPostDto.getEndDate().toString()),
//                jsonPath("$.isClosed").value(jobPostDto.isClosed())
//        };
//    }
//
//    @Test
//    @DisplayName("전체 일거리 조회 (성공)")
//    void findAll_JobPostsExist_ReturnList() throws Exception {
//        // 페이징된 데이터 설정
//        Page<JobPostDTO> jobPostPage = new PageImpl<>(jobPostDtoList);
//
//        given(jobPostService.getList(any(PageRequestDTO.class))).willReturn(jobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.totalElements").value(jobPostDtoList.size()));
//    }
//
//    @Test
//    @DisplayName("단일 일거리 조회 (성공)")
//    void readJobPost_JobPostExists_ReturnPost() throws Exception {
//        given(jobPostService.read(anyLong())).willReturn(jobPostDto);
//
//        mockMvc.perform(get("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("단일 일거리 조회 (실패) :: 검색 결과 없음")
//    void readJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//        given(jobPostService.read(anyLong())).willThrow(JobPostException.NOT_FOUND.get());
//
//        mockMvc.perform(get("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("일거리가 존재하지 않습니다."));
//    }
//
//    @Test
//    @DisplayName("일거리 등록 (성공)")
//    void registerJobPost_PostNotDuplicated_ReturnResultDto() throws Exception {
//        given(jobPostService.register(any(JobPostDTO.class))).willReturn(jobPostDto);
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(post("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("일거리 등록 (실패) :: DTO 검증 실패")
//    void registerJobPost_DtoNotValid_HttpBadRequest() throws Exception {
//        JobPostDTO invalidJobPostDTO = JobPostDTO.builder().build();  // 유효하지 않은 DTO
//
//        String content = objectMapper.writeValueAsString(invalidJobPostDTO);
//
//        mockMvc.perform(post("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("잘못된 요청입니다."));
//    }
//
//    @Test
//    @DisplayName("일거리 수정 (성공)")
//    void modifyJobPost_PostExists_ReturnResultDto() throws Exception {
//        given(jobPostService.modify(any(JobPostDTO.class))).willReturn(jobPostDto);
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(put("/api/v1/job-post/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDto));
//    }
//
//    @Test
//    @DisplayName("일거리 수정 (실패) :: 검색 결과 없음")
//    void modifyJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//        given(jobPostService.modify(any(JobPostDTO.class))).willThrow(JobPostException.NOT_FOUND.get());
//
//        String content = objectMapper.writeValueAsString(jobPostDto);
//
//        mockMvc.perform(put("/api/v1/job-post/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("일거리가 존재하지 않습니다."));
//    }
//
//    @Test
//    @DisplayName("일거리 삭제 (성공)")
//    void deleteJobPost_PostExists_ReturnSuccessMessage() throws Exception {
//        doNothing().when(jobPostService).remove(anyLong());
//
//        mockMvc.perform(delete("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("delete success"));
//    }
//
//    @Test
//    @DisplayName("일거리 삭제 (실패) :: 검색 결과 없음")
//    void deleteJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//        doThrow(JobPostException.NOT_REMOVED.get()).when(jobPostService).remove(anyLong());
//
//        mockMvc.perform(delete("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("삭제할 일거리가 존재하지 않습니다."));
//    }
//}

