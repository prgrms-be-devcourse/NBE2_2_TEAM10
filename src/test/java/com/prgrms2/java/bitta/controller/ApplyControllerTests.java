//package com.prgrms2.java.bitta.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms2.java.bitta.apply.controller.ApplyController;
//import com.prgrms2.java.bitta.apply.dto.ApplyDTO;
//import com.prgrms2.java.bitta.apply.exception.ApplyException;
//import com.prgrms2.java.bitta.apply.service.ApplyService;
//import com.prgrms2.java.bitta.jobpost.dto.JobPostDTO;
//import com.prgrms2.java.bitta.member.entity.Member;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.IntStream;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.mockito.Mockito.when;
//
//@WithMockUser
//@WebMvcTest(ApplyController.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters = false)
//public class ApplyControllerTests {
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
//    private ApplyDTO applyDTO;
//
//    private List<ApplyDTO> applyDTOList;
//
//    @BeforeAll
//    void initialize() {
//        applyDTO = ApplyDTO.builder()
//                .id(1L)
//                .jobPostId(1L)
//                .memberId(1L)
//                .appliedAt(LocalDateTime.of(2024, 10, 2, 0, 0))
//                .build();
//
//        applyDTOList = new ArrayList<>();
//        IntStream.rangeClosed(1, 5).forEach(i -> applyDTOList.add(ApplyDTO.builder()
//                .id((long) i)
//                .jobPostId(1L + i)
//                .appliedAt(LocalDateTime.of(2024, 10, 2, 0, 0))
//                .memberId((long) i)
//                .build()));
//    }
//
//    private ResultMatcher[] generateResultMatchers(ApplyDTO applyDTO) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//        String prefix = "$.";
//
//        resultMatchers.add(jsonPath(prefix + "id").value(applyDTO.getId()));
//        resultMatchers.add(jsonPath(prefix + "jobPostId").value(applyDTO.getJobPostId()));
//        resultMatchers.add(jsonPath(prefix + "memberId").value(applyDTO.getMemberId()));
//        resultMatchers.add(jsonPath(prefix + "appliedAt").value("2024-10-02T00:00:00"));
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    private ResultMatcher[] generateResultMatchers(List<ApplyDTO> applyDTOList) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//
//        for (int i = 0; i < applyDTOList.size(); i++) {
//            ApplyDTO applyDTO = applyDTOList.get(i);
//            String prefix = String.format("$.[%d].", i);
//
//            resultMatchers.add(jsonPath(prefix + "id").value(applyDTO.getId()));
//            resultMatchers.add(jsonPath(prefix + "jobPostId").value(applyDTO.getJobPostId()));
//            resultMatchers.add(jsonPath(prefix + "memberId").value(applyDTO.getMemberId()));
//            resultMatchers.add(jsonPath(prefix + "appliedAt").value("2024-10-02T00:00:00"));
//        }
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    @Test
//    @DisplayName("전체 지원서 조회 (성공)")
//    void getApply_AllApplications_ReturnList() throws Exception {
//        given(applyService.readAll(any(Member.class)))
//                .willReturn(applyDTOList);
//
//        mockMvc.perform(get("/api/v1/apply"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(applyDTOList));
//    }
//
//    @Test
//    @DisplayName("단일 지원서 조회 (성공)")
//    void getApplyById_ApplicationExists_ReturnApply() throws Exception {
//        given(applyService.readByIdAndMember(anyLong(), any(Member.class)))
//                .willReturn(applyDTO);
//
//        mockMvc.perform(get("/api/v1/apply/1"))
//                .andDo(print()) // 응답을 콘솔에 출력
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("지원서 등록 (성공)")
//    void registerApply_ApplicationRegistered_ReturnMessage() throws Exception {
//        // 테스트 응답으로 사용할 Map 생성
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "1님 지원 완료"); // memberId를 1로 가정
//        response.put("data", applyDTO);
//
//        // applyService.register()가 Map을 반환하도록 설정
//        when(applyService.register(any(ApplyDTO.class)))
//                .thenReturn(response);
//
//        // 테스트 실행
//        mockMvc.perform(post("/api/v1/apply")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(applyDTO)) // DTO를 JSON으로 변환해 요청에 전달
//                        .with(csrf())) // CSRF 보호 적용
//                .andDo(print()) // 응답 출력
//                .andExpect(status().isOk()) // 200 OK 응답 기대
//                .andExpect(jsonPath("$.message").value("1님 지원 완료")); // message 필드 검증
//    }
//
//
//    @Test
//    @DisplayName("지원서 삭제 (성공)")
//    void deleteApply_ApplicationExists_ReturnMessage() throws Exception {
//        doNothing().when(applyService).delete(anyLong());
//
//        mockMvc.perform(delete("/api/v1/apply/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message")
//                        .value("삭제가 완료되었습니다"));
//    }
//
//    @Test
//    @DisplayName("지원서 삭제 (실패) :: 존재하지 않음")
//    void deleteApply_ApplicationNotExists_NotFound() throws Exception {
//        doThrow(ApplyException.NOT_FOUND.get())
//                .when(applyService).delete(anyLong());
//
//        mockMvc.perform(delete("/api/v1/apply/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error")
//                        .value("지원서를 찾을 수 없습니다"));
//    }
//}
//
