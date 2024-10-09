package com.prgrms2.java.bitta.token.util;

import com.prgrms2.java.bitta.token.dto.TokenResponseDto;
import com.prgrms2.java.bitta.token.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;

import static io.jsonwebtoken.Jwts.SIG.HS256;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${token.grant.type}")
    private String grantType;

    @Value("${token.secret.key}")
    private String secretKey;

    private final long ONE_HOURS = 3600000L;
    private final long ONE_WEEKS = 604800000L;

    public TokenResponseDto generate(Authentication authentication) {
        String username = authentication.getName();
        String authority = authentication
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        long currentMilliseconds = System.currentTimeMillis();

        // Access Token 생성
        Date bothIssuedAt = new Date(currentMilliseconds);
        Date accessExpiredAt = new Date(currentMilliseconds + ONE_HOURS);
        Date refreshExpiredAt = new Date(currentMilliseconds + ONE_WEEKS);

        String accessToken = Jwts.builder()
                .claims(Map.of("username", username, "authority", authority))
                .issuedAt(bothIssuedAt)
                .expiration(accessExpiredAt)
                .signWith(getSignedKey(), HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("username", username)
                .issuedAt(bothIssuedAt)
                .expiration(refreshExpiredAt)
                .signWith(getSignedKey(), HS256)
                .compact();

        return TokenResponseDto.builder()
                .grantType(grantType)
                .accessToken(attachGrantType(accessToken))
                .refreshToken(attachGrantType(refreshToken))
                .build();
    }

    public TokenResponseDto reissue(String accessToken, String refreshToken) {
        accessToken = stripGrantType(accessToken);
        refreshToken = stripGrantType(refreshToken);

        if (validate(accessToken)) {
            throw TokenException.ACCESS_AVAILABLE.get();
        }

        if (!validate(refreshToken)) {
            throw TokenException.REFRESH_EXPIRED.get();
        }

        Claims claims = parseClaims(accessToken);
        String username = claims.get("username").toString();
        String authority = claims.get("authority").toString();

        long currentMillSeconds = System.currentTimeMillis();
        accessToken = Jwts.builder()
                .claims(Map.of("username", username, "authority", authority))
                .issuedAt(new Date(currentMillSeconds))
                .expiration(new Date(currentMillSeconds + ONE_HOURS))
                .signWith(getSignedKey(), HS256)
                .compact();

        return TokenResponseDto.builder()
                .grantType(grantType)
                .accessToken(attachGrantType(accessToken))
                .refreshToken(attachGrantType(refreshToken))
                .build();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignedKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("토큰 서명이 올바르지 않습니다.");
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.error("토큰을 지원하지 않습니다.");
        } catch (IllegalArgumentException e) {
            log.error("토큰이 올바르지 않습니다.");
        }

        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Object username = claims.get("username");
        Object authority = claims.get("authority");

        if (username == null || authority == null) {
            throw TokenException.EMPTY_CLAIMS.get();
        }

        Collection<? extends GrantedAuthority> authorities
                = Collections.singletonList(new SimpleGrantedAuthority(authority.toString()));

        authorities.forEach(System.out::println);

        return new UsernamePasswordAuthenticationToken(username.toString(), "", authorities);
    }

    private String stripGrantType(String token) {
        if (!StringUtils.hasText(token)) {
            throw TokenException.EMPTY_HEADER.get();
        }

        if (!token.startsWith(grantType)) {
            throw TokenException.WRONG_GRANT_TYPE.get();
        }

        return token.substring(grantType.length() + 1);
    }

    private String attachGrantType(String token) {
        return String.format("%s %s", grantType, token);
    }

    private SecretKey getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignedKey())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            throw TokenException.BAD_SIGNATURE.get();
        } catch (ExpiredJwtException e) {
            throw TokenException.TOKEN_EXPIRED.get();
        } catch (UnsupportedJwtException e) {
            throw TokenException.UNSUPPORTED.get();
        } catch (IllegalArgumentException e) {
            throw TokenException.WRONG_TOKEN.get();
        }
    }
}