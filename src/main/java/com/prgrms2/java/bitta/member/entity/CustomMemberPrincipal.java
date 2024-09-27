package com.prgrms2.java.bitta.member.entity;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomMemberPrincipal implements Principal {
    private final String email;

    @Override
    public String getName() {
        return email;
    }
}