package com.prgrms2.java.bitta.token.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface ReissueService {
    ResponseEntity<?> reissueTokens(HttpServletRequest request, HttpServletResponse response);
}