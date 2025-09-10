package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.oracle.repository.dto.LoginDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) {
        try {
            log.info("Login Request {}", loginRequest.getUsername());

            LoginResult result = dashboardService.login(loginRequest.getUsername(), loginRequest.getPassword());

            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid username or password. Please try again."));
            }

            log.info("Login Request Result {} {}", loginRequest.getUsername(), result);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Something went wrong. Please try again later."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("username") String username
    ) {
        try {
            log.info("Logout Request {}", username);
            boolean result = dashboardService.logout(sessionId, username);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Logout failed. Please try again."));
        }
    }

    // Reusable error response DTO
    record ErrorResponse(String message) {}
}
