package com.ciptadana.dashboard.security;

import com.ciptadana.dashboard.database.oracle.repository.jpa.DataTransactionJpaRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final DataTransactionJpaRepositoryOracle dataTransactionRepositoryOracle;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Ambil sessionId dari header "Authorization"
            String headerAuth = request.getHeader("Authorization");
            String sessionId = null;

            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                sessionId = headerAuth.substring(7);
            }

            if (sessionId != null) {
                // 2. Validasi sessionId ke database
                LoginResult sessionData = dataTransactionRepositoryOracle.getSessionStatus(sessionId);

                if (sessionData != null) {
                    // 3. Jika valid, buat objek autentikasi dan simpan di Security Context
                    UserDetails userDetails = new User(sessionData.getUsername(), "", new ArrayList<>());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // 3. Gunakan 'log' (dari @Slf4j) untuk mencatat error
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // Lanjutkan permintaan ke filter berikutnya atau ke controller
        filterChain.doFilter(request, response);
    }
}