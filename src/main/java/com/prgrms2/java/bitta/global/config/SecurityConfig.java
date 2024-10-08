package com.prgrms2.java.bitta.global.config;

import com.prgrms2.java.bitta.token.filter.TokenAuthenticationFilter;
import com.prgrms2.java.bitta.token.util.TokenProvider;
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
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "api/v1/members/sign-up",
                                "api/v1/members/sign-in",
                                "api/v1/members/refresh").permitAll()
                        .requestMatchers(
                                "api/v1/members/test",
                                "api/v1/members/{id}",
                                "api/v1/feed/**",
                                "api/v1/apply/**",
                                "api/v1/job-post/**",
                                "api/v1/scout/**").hasRole("USER")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                        // .requestMatchers("/images/**").permitAll() /  S3 연결 필요
                        .anyRequest().authenticated())
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
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
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}