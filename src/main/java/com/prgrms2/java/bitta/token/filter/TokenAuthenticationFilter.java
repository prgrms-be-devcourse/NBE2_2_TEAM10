package com.prgrms2.java.bitta.token.filter;

import com.prgrms2.java.bitta.token.exception.TokenException;
import com.prgrms2.java.bitta.token.util.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${token.grant.type}")
    private String grantType;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = parseToken(request);

            if (tokenProvider.validate(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Setting authentication for user: {}", authentication.getName());
            }
        } catch (RuntimeException ignored) {}

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");

        if (!StringUtils.hasText(accessToken)) {
            throw TokenException.EMPTY_HEADER.get();
        }

        if (!accessToken.startsWith(grantType)) {
            throw TokenException.WRONG_GRANT_TYPE.get();
        }

        return accessToken.substring(grantType.length() + 1);
    }
}