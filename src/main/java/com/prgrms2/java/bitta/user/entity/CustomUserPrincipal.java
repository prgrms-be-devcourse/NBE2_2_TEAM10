package com.prgrms2.java.bitta.user.entity;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomUserPrincipal implements Principal {
    private final String email;

    @Override
    public String getName() {
        return email;
    }
}