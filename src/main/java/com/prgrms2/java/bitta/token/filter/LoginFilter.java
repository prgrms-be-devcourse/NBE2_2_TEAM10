package com.prgrms2.java.bitta.token.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms2.java.bitta.member.dto.LoginDTO;
import com.prgrms2.java.bitta.token.entity.RefreshEntity;
import com.prgrms2.java.bitta.token.repository.RefreshRepository;
import com.prgrms2.java.bitta.token.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDTO loginDTO = new LoginDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        System.out.println(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
