package com.prgrms2.java.bitta.token.service;

import com.prgrms2.java.bitta.token.entity.RefreshEntity;
import com.prgrms2.java.bitta.token.repository.RefreshRepository;
import com.prgrms2.java.bitta.token.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueServiceImpl implements ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueServiceImpl(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public ResponseEntity<?> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refresh = extractRefreshToken(request);
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        if (!isValidRefreshToken(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        updateRefreshToken(refresh, username, newRefresh);

        setResponseTokens(response, newAccess, newRefresh);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isValidRefreshToken(String refresh) {
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return false;
        }
        return refreshRepository.existsByRefresh(refresh);
    }

    private void updateRefreshToken(String oldRefresh, String username, String newRefresh) {
        refreshRepository.deleteByRefresh(oldRefresh);
        addRefreshEntity(username, newRefresh, 86400000L);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }

    private void setResponseTokens(HttpServletResponse response, String access, String refresh) {
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}