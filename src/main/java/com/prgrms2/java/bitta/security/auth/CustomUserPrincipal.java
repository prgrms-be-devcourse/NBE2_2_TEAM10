package com.prgrms2.java.bitta.security.auth;

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