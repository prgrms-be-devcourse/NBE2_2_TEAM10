package com.prgrms2.java.bitta.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 60000);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities.isEmpty() ? "ROLE_USER" : authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 - 권한 정보 제외
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 갱신 처리 메서드
    public JwtToken reissueToken(String accessToken, String refreshToken) {
        // 1. Access Token이 아직 유효한 경우
        if (validateToken(accessToken)) {
            return null; // 아직 유효하므로 재발급 필요 없음
        }

        // 2. Refresh Token 검증
        try {
            if (!validateToken(refreshToken)) {
                throw new RuntimeException("Refresh Token이 유효하지 않습니다. 다시 로그인해주세요.");
            }

            // Refresh Token에서 사용자 정보 추출
            Claims refreshClaims = parseClaims(refreshToken);
            String username = refreshClaims.getSubject();

            // Access Token에서 권한 정보 추출
            Claims accessClaims = parseClaims(accessToken);
            String authorities = accessClaims.get("auth", String.class);

            // 새로운 Access Token 생성
            long now = (new Date()).getTime();
            Date accessTokenExpiresIn = new Date(now + 60000);
            String newAccessToken = Jwts.builder()
                    .setSubject(username)
                    .claim("auth", authorities)
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return JwtToken.builder()
                    .grantType("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (ExpiredJwtException e) {
            log.info("Refresh Token이 만료되었습니다. 로그아웃 처리됩니다.");
            throw new RuntimeException("Refresh Token이 만료되었습니다. 다시 로그인해주세요.");
        }
    }

    // 나머지 메서드들은 그대로 유지
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}