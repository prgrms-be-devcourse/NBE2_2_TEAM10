package com.prgrms2.java.bitta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.jobpost.entity.Location;
import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.entity.Role;
import com.prgrms2.java.bitta.member.exception.MemberException;
import com.prgrms2.java.bitta.member.service.MemberService;
import com.prgrms2.java.bitta.token.controller.TokenController;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenControllerTests {
    @MockBean
    private MemberService memberService;

    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberDTO memberDto;

    private Map<String, Object> payloadMap;

    @BeforeAll
    void initialize() {
        memberDto = MemberDTO.builder()
                .memberId(1L)
                .memberName("name")
                .email("name@email.com")
                .password("password")
                .location(Location.SEOUL.name())
                .profilePicture("pictureURL")
                .role(Role.Actor)
                .createdAt(LocalDateTime.now())
                .build();

        payloadMap = new HashMap<>() {{
            put("memberId", 1L);
            put("memberName", "name");
            put("email", "name@email.com");
            put("role", Role.Actor);
        }};
    }

    @Test
    @DisplayName("회원 로그인 (성공)")
    void login_MemberValid_ReturnBothTokens() throws Exception {
        given(memberService.read(anyString(), anyString()))
                .willReturn(memberDto);

        given(memberDto.getPayload())
                .willReturn(payloadMap);

        given(jwtUtil.createToken(payloadMap, 60))
                .willReturn("FakeAccessToken");

        given(jwtUtil.createToken(Map.of("email", "name@email.com"), 60 * 24 * 7))
                .willReturn("FakeRefreshToken");

        String content = objectMapper.writeValueAsString(memberDto);

        mockMvc.perform(post("/api/v1/token/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("FakeAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("FakeRefreshToken"));
    }

    @Test
    @DisplayName("회원 로그인 (실패) :: 잘못된 회원정보")
    void login_MemberNotValid_ReturnStatusUnauthorized() throws Exception {
        given(memberService.read(anyString(), anyString()))
                .willThrow(MemberException.BAD_CREDENTIALS.get());

        String content = objectMapper.writeValueAsString(memberDto);

        mockMvc.perform(post("/api/v1/token/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value(MemberException.BAD_CREDENTIALS));
    }
}
