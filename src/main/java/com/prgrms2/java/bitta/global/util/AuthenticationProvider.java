package com.prgrms2.java.bitta.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthenticationProvider {
    public static String getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }

        return authentication.getName();
    }

    public static String getRoles() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            return "ROLE_USER"; // 기본값을 설정하거나 빈 문자열 반환
        }

        // 첫 번째 권한의 이름을 문자열로 반환
        return authentication.getAuthorities().iterator().next().getAuthority();
    }
}