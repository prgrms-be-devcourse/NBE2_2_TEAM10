//package com.prgrms2.java.bitta.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
//import com.prgrms2.java.bitta.apply.repository.ApplyRepository;
//import com.prgrms2.java.bitta.apply.service.ApplyService;
//import com.prgrms2.java.bitta.jobpost.controller.JobPostController;
//import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
//import com.prgrms2.java.bitta.global.dto.PageRequestDTO;
//import com.prgrms2.java.bitta.jobpost.entity.JobPost;
//import com.prgrms2.java.bitta.jobpost.entity.Location;
//import com.prgrms2.java.bitta.jobpost.entity.PayStatus;
//import com.prgrms2.java.bitta.jobpost.entity.ShootMethod;
//import com.prgrms2.java.bitta.jobpost.exception.JobPostException;
//import com.prgrms2.java.bitta.jobpost.repository.JobPostRepository;
//import com.prgrms2.java.bitta.jobpost.service.JobPostService;
//import com.prgrms2.java.bitta.member.entity.Member;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WithMockUser
//@WebMvcTest(JobPostController.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters = false)
//public class JobPostControllerTests {
//
//    @MockBean
//    private JobPostService jobPostService;
//
//    @MockBean
//    private ApplyService applyService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private JobPostDTO jobPostDTO;
//    private List<JobPostDTO> jobPostDTOList;
//    private List<ApplyDTO> applyDTOList;
//
//    @MockBean
//    private JobPostRepository jobPostRepository;
//    @MockBean
//    private ApplyRepository applyRepository;
//
//    private Member member;
//
//    @BeforeEach
//    void initialize() {
//        member = Member.builder()
//                .id(1L)
//                .username("testUser")
//                .build();
//
//        applyDTOList = new ArrayList<>();
//
//        IntStream.rangeClosed(1, 5).forEach(i ->
//                applyDTOList.add(ApplyDTO.builder()
//                        .id((long) i)
//                        .jobPostId(1L)
//                        .memberId(1L)
//                        .appliedAt(LocalDateTime.now())
//                        .build()));
//
//        jobPostDTO = JobPostDTO.builder()
//                .id(1L)
//                .memberId(1L)
//                .title("Title")
//                .description("Description")
//                .location(Location.SEOUL)
//                .payStatus(PayStatus.FREE)
//                .updateAt(LocalDateTime.now().withNano(0))
//                .shootMethod(ShootMethod.FILM)
//                .auditionDate(LocalDate.now())
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.now().plusDays(7))
//                .isClosed(false)
//                .build();
//
//        jobPostDTOList = new ArrayList<>();
//
//        IntStream.rangeClosed(1, 5).forEach(i ->
//            jobPostDTOList.add(JobPostDTO.builder()
//                    .id((long) i)
//                    .memberId(1L)
//                    .title("Title" + i)
//                    .description("Description" + i)
//                    .location(Location.SEOUL)
//                    .payStatus(PayStatus.FREE)
//                    .shootMethod(ShootMethod.FILM)
//                    .auditionDate(LocalDate.now())
//                    .updateAt(LocalDateTime.now().withNano(0))
//                    .startDate(LocalDate.now())
//                    .endDate(LocalDate.now().plusDays(7))
//                    .isClosed(false)
//                    .build()));
//    }
//
//    private ResultMatcher[] generateResultMatchers(JobPostDTO jobPostDTO) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//        String prefix = "$.";
//
//        resultMatchers.add(jsonPath(prefix + "id").value(jobPostDTO.getId()));
//        resultMatchers.add(jsonPath(prefix + "memberId").value(jobPostDTO.getMemberId()));
//        resultMatchers.add(jsonPath(prefix + "title").value(jobPostDTO.getTitle()));
//        resultMatchers.add(jsonPath(prefix + "description").value(jobPostDTO.getDescription()));
//        resultMatchers.add(jsonPath(prefix + "location").value(jobPostDTO.getLocation().toString()));
//        resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDTO.getPayStatus().toString()));
//        resultMatchers.add(jsonPath(prefix + "shootMethod").value(jobPostDTO.getShootMethod().toString()));
//        resultMatchers.add(jsonPath(prefix + "auditionDate").value(jobPostDTO.getAuditionDate().toString()));
//        resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDTO.getUpdateAt().withNano(0).toString()));
//        resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDTO.getStartDate().toString()));
//        resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDTO.getEndDate().toString()));
//        resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDTO.isClosed()));
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    private ResultMatcher[] generateResultMatchers(List<JobPostDTO> jobPostDTOList) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//
//        for (int i = 0; i < jobPostDTOList.size(); i++) {
//            JobPostDTO jobPostDTO = jobPostDTOList.get(i);
//            String prefix = String.format("$.result[%d].", i);
//
//            resultMatchers.add(jsonPath(prefix + "id").value(jobPostDTO.getId()));
//            resultMatchers.add(jsonPath(prefix + "memberId").value(jobPostDTO.getMemberId()));
//            resultMatchers.add(jsonPath(prefix + "title").value(jobPostDTO.getTitle()));
//            resultMatchers.add(jsonPath(prefix + "description").value(jobPostDTO.getDescription()));
//            resultMatchers.add(jsonPath(prefix + "location").value(jobPostDTO.getLocation().toString()));
//            resultMatchers.add(jsonPath(prefix + "payStatus").value(jobPostDTO.getPayStatus().toString()));
//            resultMatchers.add(jsonPath(prefix + "shootMethod").value(jobPostDTO.getShootMethod().toString()));
//            resultMatchers.add(jsonPath(prefix + "auditionDate").value(jobPostDTO.getAuditionDate()));
//            resultMatchers.add(jsonPath(prefix + "updateAt").value(jobPostDTO.getUpdateAt()));
//            resultMatchers.add(jsonPath(prefix + "startDate").value(jobPostDTO.getStartDate()));
//            resultMatchers.add(jsonPath(prefix + "endDate").value(jobPostDTO.getEndDate()));
//            resultMatchers.add(jsonPath(prefix + "isClosed").value(jobPostDTO.isClosed()));
//        }
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    @Test
//    @DisplayName("전체 일거리 조회 (성공)")
//    void findAll_JobPostsExist_ReturnList() throws Exception {
//        Page<JobPostDTO> jobPostPage = new PageImpl<>(jobPostDTOList);
//
//        given(jobPostService.getList(any(PageRequestDTO.class))).willReturn(jobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.totalElements").value(jobPostDTOList.size()));
//    }
//
//    @Test
//    @DisplayName("단일 일거리 조회 (성공)")
//    void readJobPost_JobPostExists_ReturnPost() throws Exception {
//        given(jobPostService.read(anyLong())).willReturn(jobPostDTO);
//
//        mockMvc.perform(get("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDTO));
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
//                .andExpect(jsonPath("$.error").value("게시글을 찾을 수 없습니다"));
//    }
//
//    @Test
//    @DisplayName("일거리 등록 (성공)")
//    void registerJobPost_PostNotDuplicated_ReturnResultDto() throws Exception {
//        given(jobPostService.register(any(JobPostDTO.class))).willReturn(jobPostDTO);
//
//        String content = objectMapper.writeValueAsString(jobPostDTO);
//
//        mockMvc.perform(post("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDTO));
//    }
//
//    @Test
//    @DisplayName("JobPost 등록 (실패) :: 유효성 검증 실패로 BadRequest 반환")
//    void createJobPost_DtoValidationFails_HttpStatusBadRequest() throws Exception {
//        JobPostDTO invalidJobPostDTO = new JobPostDTO(
//                null,
//                1L,
//                null,
//                " ",
//                Location.BUSAN,
//                PayStatus.PAID,
//                LocalDateTime.now(),
//                ShootMethod.FILM,
//                LocalDate.of(2024, 11, 01),
//                LocalDate.of(2024,10,2),
//                LocalDate.of(2024,10,3),
//                true
//        );
//
//        mockMvc.perform(post("/api/v1/job-post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidJobPostDTO))
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    @DisplayName("일거리 수정 (성공)")
//    void modifyJobPost_PostExists_ReturnResultDto() throws Exception {
//        given(jobPostService.modify(any(JobPostDTO.class))).willReturn(jobPostDTO);
//
//        String content = objectMapper.writeValueAsString(jobPostDTO);
//
//        mockMvc.perform(put("/api/v1/job-post/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(jobPostDTO));
//    }
//
//    @Test
//    @DisplayName("일거리 수정 (실패) :: 검색 결과 없음")
//    void modifyJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//        given(jobPostService.modify(any(JobPostDTO.class))).willThrow(JobPostException.NOT_FOUND.get());
//
//        String content = objectMapper.writeValueAsString(jobPostDTO);
//
//        mockMvc.perform(put("/api/v1/job-post/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("게시글을 찾을 수 없습니다"));
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
//                .andExpect(jsonPath("$.message").value("삭제가 완료되었습니다"));
//    }
//
//    @Test
//    @DisplayName("일거리 삭제 (실패) :: 검색 결과 없음")
//    void deleteJobPost_PostNotExists_HttpStatusNotFound() throws Exception {
//        doThrow(JobPostException.NOT_REMOVED.get()).when(jobPostService).remove(anyLong());
//
//        mockMvc.perform(delete("/api/v1/job-post/1"))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("게시글을 삭제할 수 없습니다"));
//    }
//
//    @Test
//    @DisplayName("특정 작성자가 작성한 다른 게시물 조회 (성공)")
//    void getJobPostByMember_ValidMemberId_ReturnsPagedResults() throws Exception {
//        Page<JobPostDTO> jobPostPage = new PageImpl<>(jobPostDTOList);
//
//        given(jobPostService.getJobPostByMember(anyLong(), any(PageRequestDTO.class))).willReturn(jobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post/member/1")
//                        .param("page", "0")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content.length()").value(jobPostDTOList.size()))
//                .andExpect(jsonPath("$.totalElements").value(jobPostDTOList.size()))
//                .andExpect(jsonPath("$.totalPages").value(1));
//    }
//
//    @Test
//    @DisplayName("특정 작성자가 작성한 다른 게시물 조회 (실패) :: 작성한 게시물이 없음")
//    void getJobPostByMember_NoPosts_ReturnsEmptyPagedResults() throws Exception {
//        Page<JobPostDTO> emptyJobPostPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);
//
//        given(jobPostService.getJobPostByMember(anyLong(), any(PageRequestDTO.class))).willReturn(emptyJobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post/member/1")
//                        .param("page", "0")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content.length()").value(0))
//                .andExpect(jsonPath("$.totalElements").value(0))
//                .andExpect(jsonPath("$.totalPages").value(0));
//    }
//
//
//    @Test
//    @DisplayName("JobPost 검색 (성공)")
//    void searchJobPost_ValidKeyword_ReturnsPagedResults() throws Exception {
//        Page<JobPostDTO> jobPostPage = new PageImpl<>(jobPostDTOList);
//
//        given(jobPostService.searchJobPosts(anyString(), any(PageRequestDTO.class))).willReturn(jobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post/search")
//                        .param("keyword", "Title")
//                        .param("page", "0")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content.length()").value(jobPostDTOList.size()))
//                .andExpect(jsonPath("$.totalElements").value(jobPostDTOList.size()))
//                .andExpect(jsonPath("$.totalPages").value(1));
//    }
//
//    @Test
//    @DisplayName("JobPost 검색 (실패) :: 검색 결과 없음")
//    void searchJobPost_NoResults_ReturnsEmptyPagedResults() throws Exception {
//        Page<JobPostDTO> emptyJobPostPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);
//
//        given(jobPostService.searchJobPosts(anyString(), any(PageRequestDTO.class))).willReturn(emptyJobPostPage);
//
//        mockMvc.perform(get("/api/v1/job-post/search")
//                        .param("keyword", "NonExistingKeyword")
//                        .param("page", "0")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())  // 성공적인 응답 확인
//                .andExpect(jsonPath("$.content").isArray())
//                .andExpect(jsonPath("$.content.length()").value(0))
//                .andExpect(jsonPath("$.totalElements").value(0))
//                .andExpect(jsonPath("$.totalPages").value(0));
//    }
//
//    @Test
//    @DisplayName("특정 게시글에 대한 Apply 리스트 조회 (성공)")
//    void getApplyForJobPost_Success() throws Exception {
//        given(applyService.getApplyForJobPost(anyLong())).willReturn(applyDTOList);
//
//        mockMvc.perform(get("/api/v1/job-post/1/showApply")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(applyDTOList.size()))
//                .andExpect(jsonPath("$[0].id").value(applyDTOList.get(0).getId()))
//                .andExpect(jsonPath("$[0].jobPostId").value(applyDTOList.get(0).getJobPostId()));
//    }
//
//    @Test
//    @DisplayName("특정 게시글에 대한 Apply 리스트 조회 (게시글 없음)")
//    void getApplyForJobPost_NotFound() throws Exception {
//        given(applyService.getApplyForJobPost(anyLong())).willThrow(JobPostException.NOT_FOUND.get());
//
//        mockMvc.perform(get("/api/v1/job-post/1/apply")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("게시글을 찾을 수 없습니다"));
//    }
//}
//
