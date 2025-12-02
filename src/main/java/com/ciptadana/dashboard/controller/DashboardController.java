package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.oracle.repository.dto.ChangePasswordDTO;
import com.ciptadana.dashboard.database.oracle.repository.dto.ChangePasswordResponse;
import com.ciptadana.dashboard.database.oracle.repository.dto.LoginDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.exception.UnauthorizedException;
import com.ciptadana.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

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

            // Validate User Type & Client Code
            String clientcode = dashboardService.validateUserType(loginRequest.getUsername());
            if (clientcode == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.from("Username does not have login access to dashboard."));
            }

            if (clientcode.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.from("ClientCode for this user is not registered (empty)."));
            }

            // Validate Account Enabled
            Integer accEnabled = dashboardService.validateAccEnabled(loginRequest.getUsername());

            if (accEnabled == null || accEnabled <= 0) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Your account is disabled. Please contact customer service for more information."));
            }

            // If validation is successful, proceed to the login process (check password)
            LoginResult result = dashboardService.login(loginRequest.getUsername(), loginRequest.getPassword());

            if (result == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Invalid username or password. Please try again."));
            }

            log.info("Login Request Result {} {}", loginRequest.getUsername(), result);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.from("Something went wrong. Please try again later."));
        }
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(
//            @RequestParam("sessionId") String sessionId,
//            @RequestParam("username") String username
//    ) {
//        try {
//            log.info("Logout Request {}", username);
//            boolean result = dashboardService.logout(sessionId, username);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Logout error: {}", e.getMessage(), e);
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse("Logout failed. Please try again."));
//        }
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            log.info("Logout Request {}", logoutRequest.username());
            boolean result = dashboardService.logout(
                    logoutRequest.sessionId(),
                    logoutRequest.username()
            );

            return ResponseEntity.ok(Map.of(
                    "success", result,
                    "message", "Logout successful"
            ));

        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage(), e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Logout failed. Please try again."
                    ));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChangePasswordDTO changePasswordRequest) {
        try {
            // Extract sessionId from Bearer token
            log.info("Authorization header: {}", authorizationHeader);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Missing or invalid Authorization header."));
            }

            String sessionId = authorizationHeader.replace("Bearer ", "").trim();
            log.info("Change password request for session: {}", sessionId);

            if (sessionId.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Session not found. Please login again."));
            }

            // Validate active session
            LoginResult session;
            try {
                session = dashboardService.validateActiveSession(sessionId);
            } catch (UnauthorizedException e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from(e.getMessage()));
            }

            // Check session status
            if (session == null || session.getStatus() != 0) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Session is invalid or expired"));
            }

            String username = session.getUsername();
            log.info("Change password for user: {}", username);

            // Validate old password
            boolean isOldPasswordValid = dashboardService.validatePassword(
                    username,
                    changePasswordRequest.getOldPassword()
            );

            if (!isOldPasswordValid) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.fromField("oldPassword", "Current password is incorrect."));
            }

            // Validate new password format
            String passwordValidationError = validatePasswordFormat(changePasswordRequest.getNewPassword());
            if (passwordValidationError != null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.fromField("newPassword", passwordValidationError));
            }

            // Check if new password is same as old password
            if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.fromField("newPassword", "New password must be different from current password."));
            }

            // Update password
            boolean success = dashboardService.changePassword(
                    username,
                    changePasswordRequest.getNewPassword()
            );

            if (success) {
                log.info("Password changed successfully for user: {}", username);
                return ResponseEntity.ok(new ChangePasswordResponse(
                        true,
                        "Password changed successfully"
                ));
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiError.from("Failed to change password. Please try again."));
            }

        } catch (Exception e) {
            log.error("Change password error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.from("Something went wrong. Please try again later."));
        }
    }
    
    @GetMapping("/validate-session")
    public ResponseEntity<?> validateSession(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from("Missing or invalid Authorization header."));
            }

            String sessionId = authorizationHeader.replace("Bearer ", "").trim();

            try {
                LoginResult session = dashboardService.validateActiveSession(sessionId);

                if (session == null || session.getStatus() != 0) {
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(ApiError.from("Session is invalid or expired"));
                }

                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "username", session.getUsername()
                ));

            } catch (UnauthorizedException e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiError.from(e.getMessage()));
            }

        } catch (Exception e) {
            log.error("Session validation error: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.from("Something went wrong."));
        }
    }

    // Helper method for password validation
    private String validatePasswordFormat(String password) {
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }

        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number.";
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\/;'`~].*")) {
            return "Password must contain at least one special character.";
        }

        return null; // Password is valid
    }

    // Reusable error response DTO
    record ErrorResponse(String message) {}

    record LogoutRequest(String sessionId, String username) {}

    record ApiError(String timestamp, Map<String, String> errors) {
        public static ApiError from(String errorMessage) {
            return new ApiError(
                    Instant.now().toString(),
                    Map.of("auth", errorMessage)
            );
        }

        public static ApiError fromField(String field, String errorMessage) {
            return new ApiError(
                    Instant.now().toString(),
                    Map.of(field, errorMessage)
            );
        }
    }
}
