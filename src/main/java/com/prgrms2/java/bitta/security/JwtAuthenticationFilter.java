package com.prgrms2.java.bitta.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 디버깅 로그 추가
        log.info("Token Extracted: {}", token);

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰이 유효할 경우 Authentication 객체를 SecurityContext에 저장
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 디버깅 로그 추가: Authentication이 SecurityContext에 설정되었는지 확인
                log.info("Authentication set to SecurityContext: {}", authentication.getName());
            } catch (RuntimeException e) {
                log.error("Authentication failed: {}", e.getMessage());
            }
        } else {
            log.warn("Invalid or missing JWT token.");
        }
        // 4. 필터 체인 진행
        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            // "Bearer " 뒤의 공백을 제거하고 토큰 추출
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}