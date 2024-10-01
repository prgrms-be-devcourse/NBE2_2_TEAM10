//package com.prgrms2.java.bitta.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms2.java.bitta.jobpost.entity.Location;
//import com.prgrms2.java.bitta.member.controller.MemberController;
//import com.prgrms2.java.bitta.member.dto.MemberDTO;
//import com.prgrms2.java.bitta.member.entity.Role;
//import com.prgrms2.java.bitta.member.exception.MemberException;
//import com.prgrms2.java.bitta.member.service.MemberServiceImpl;
//import com.prgrms2.java.bitta.token.util.JWTUtil;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(MemberController.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class MemberControllerTests {
//    @MockBean
//    private MemberServiceImpl memberServiceImpl;
//
//    @MockBean
//    private MemberRepository memberRepository;
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
//    private static MemberDTO memberDto;
//
//    @BeforeAll
//    static void initialize() {
//        memberDto = MemberDTO.builder()
//                .memberId(1L)
//                .memberName("name")
//                .email("name@email.com")
//                .password("password")
//                .location(Location.SEOUL.name())
//                .profilePicture("pictureURL")
//                .role(Role.Actor)
//                .createdAt(LocalDateTime.now())
//                .build();
//    }
//
//    private ResultMatcher[] generateResultMatchers(MemberDTO memberDto) {
//        List<ResultMatcher> resultMatchers = new ArrayList<>();
//        String prefix = "$.";
//
//        resultMatchers.add(jsonPath(prefix + "memberId").value(memberDto.getMemberId()));
//        resultMatchers.add(jsonPath(prefix + "memberName").value(memberDto.getMemberName()));
//        resultMatchers.add(jsonPath(prefix + "email").value(memberDto.getEmail()));
//        resultMatchers.add(jsonPath(prefix + "password").value(memberDto.getPassword()));
//        resultMatchers.add(jsonPath(prefix + "location").value(memberDto.getLocation()));
//        resultMatchers.add(jsonPath(prefix + "profilePicture").value(memberDto.getProfilePicture()));
//        resultMatchers.add(jsonPath(prefix + "role").value(memberDto.getRole()));
//        resultMatchers.add(jsonPath(prefix + "createdAt").value(memberDto.getCreatedAt()));
//
//        return resultMatchers.toArray(ResultMatcher[]::new);
//    }
//
//    @Test
//    @DisplayName("회원 정보 조회 (성공)")
//    void getMemberInfo_EmailIsValid_ReturnResultDto() throws Exception {
//        Principal mockPrincipal = mock(Principal.class);
//
//        given(mockPrincipal.getName())
//                .willReturn("name@email.com");
//
//        given(memberServiceImpl.read(anyString()))
//                .willReturn(memberDto);
//
//        mockMvc.perform(get("/api/v1/member")
//                .principal(mockPrincipal))
//                .andExpect(status().isOk())
//                .andExpectAll(generateResultMatchers(memberDto));
//    }
//
//    @Test
//    @DisplayName("회원 정보 조회 (실패) :: Principal 검증 싪패")
//    void getMemberInfo_PrincipalNotValid_HttpStatusUnauthorized() throws Exception {
//
//    }
//
//    @Test
//    @DisplayName("회원 정보 조회 (실패) :: 이메일 검증 실패")
//    void getMemberInfo_EmailNotValid_ReturnStatusUnauthorized() throws Exception {
//        Principal mockPrincipal = mock(Principal.class);
//
//        given(mockPrincipal.getName())
//                .willReturn("name@email.com");
//
//        given(memberServiceImpl.read(anyString()))
//                .willThrow(MemberException.BAD_CREDENTIALS.get());
//
//        mockMvc.perform(get("/api/v1/member")
//                .principal(mockPrincipal))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message")
//                        .value(MemberException.BAD_CREDENTIALS));
//    }
//}
