package com.prgrms2.java.bitta.token.filter;

import com.prgrms2.java.bitta.token.util.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = parseToken(request);

        if (accessToken != null && tokenProvider.validate(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Setting authentication for user: {}", authentication.getName());
        } else {
            log.warn("Invalid token, proceeding without setting authentication.");
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");

        if (!StringUtils.hasText(accessToken) || !accessToken.startsWith("Bearer")) {
            log.warn("인증 헤더가 비어있거나 일치하지 않습니다.");
            return null;
        }

        return accessToken.substring("Bearer".length() + 1);
    }
}