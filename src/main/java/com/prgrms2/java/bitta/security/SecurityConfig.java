package com.prgrms2.java.bitta.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/members/sign-in").permitAll()
                        .requestMatchers("/members/sign-up").permitAll()
                        .requestMatchers("/members/test").hasRole("USER")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        //접근 패턴 - 모든 출처에서의 요청 허락
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        //허용 메서드
        corsConfig.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE"));
        // 허용 헤더
        corsConfig.setAllowedHeaders(
                List.of("Authorization",
                        "Content-Type",
                        "Cache-Control"));

        //자격 증명 허용 여부
        corsConfig.setAllowCredentials(true);

        //URL 패턴 기반으로 CORS 구성
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();

        corsSource.registerCorsConfiguration(("/**"), corsConfig);
        return corsSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}