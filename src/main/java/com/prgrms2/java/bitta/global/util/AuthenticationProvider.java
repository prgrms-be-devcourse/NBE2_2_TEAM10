package com.prgrms2.java.bitta.global.util;

import com.prgrms2.java.bitta.member.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;


public class AuthenticationProvider {
    public static String getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }

        return authentication.getName();
    }

    public static Role getRoles() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new RuntimeException("");
        }

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        return Role.valueOf(role);
    }
}