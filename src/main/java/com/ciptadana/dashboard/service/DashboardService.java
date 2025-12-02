package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.oracle.repository.DataTransactionRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {


    private int STATUS_LOGIN  = 0;
    private int STATUS_LOGOUT = 1;
    private final DataTransactionRepositoryOracle dataTransactionRepositoryOracle;

    public LoginResult login(String username, String password) {

        LoginResult result = null;
        try {
            LocalDateTime today = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss:SSS");
            String now = today.format(formatter);

            String data = dataTransactionRepositoryOracle.login(username, password);
            if (data == null) {
                log.warn("Login gagal untuk username {}", username);
                return null;
            }

            // ✅ 1. invalidate all sessionId
            dataTransactionRepositoryOracle.invalidateOldSessions(username, 1, now);

            // ✅ 2. create new sessionId
            String sessionId = generateNumericSession(12);
            dataTransactionRepositoryOracle.insertSessionLogin(sessionId, now, username, 0, now);

            result = dataTransactionRepositoryOracle.getSessionStatus(sessionId);

        } catch (Exception e) {
            log.error("Error saat login user {}: {}", username, e.getMessage(), e);
        }
        return result;
    }



    /**public LoginResult login(String username, String password){

     LoginResult result = null;
     try {

     LocalDateTime today = LocalDateTime.now();
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss:SSS");
     String data = dataTransactionRepositoryOracle.login(username, password);
     String sessionId = generateNumericSession(12);

     if(data != null){
     dataTransactionRepositoryOracle.insertSessionLogin(sessionId,
     today.format(formatter),
     username,
     STATUS_LOGIN,
     today.format(formatter));

     result = dataTransactionRepositoryOracle.getSessionStatus(sessionId);
     }

     }catch (Exception e){
     log.error(e.getMessage());
     }
     return result;
     }**/

    public boolean logout(String sessionId, String username) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss:SSS");
            String updatedTime = now.format(formatter);

            log.info("Attempting logout for user: {} with sessionId: {}", username, sessionId);

            dataTransactionRepositoryOracle.updateSessionLogout(
                    sessionId,
                    username,
                    STATUS_LOGOUT,
                    updatedTime
            );

            log.info("Logout successful for user: {}", username);
            return true;

        } catch (Exception e) {
            log.error("Logout failed for user: {} with sessionId: {}. Error: {}",
                    username, sessionId, e.getMessage(), e);
            return false;
        }
    }

    private String generateNumericSession(int length) {
//        Random random = new Random();
//        StringBuilder session = new StringBuilder();

        SecureRandom random = new SecureRandom();
        StringBuilder session = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            session.append(random.nextInt(10));
        }

        return session.toString();
    }

    public LoginResult validateActiveSession(String sessionId) {
        LoginResult session = dataTransactionRepositoryOracle.getSessionStatus(sessionId);
        if (session == null) {
            throw new UnauthorizedException("Session tidak ditemukan");
        }
        if (session.getStatus() != STATUS_LOGIN) {
            throw new UnauthorizedException("Session sudah expired / logout");
        }
        return session;
    }

    public String validateUserType(String username){
        List<String> results = dataTransactionRepositoryOracle.getUserType(username);

        // Check if the query RETURNS NO ROWS AT ALL
        if (results.isEmpty()) {
            return null;
        }

        String clientCode = results.get(0);

        // Check if the query returns rows, BUT the column value is empty.
        if (clientCode == null) {
            return "";
        }

        return clientCode;
    }

    public Integer validateAccEnabled(String username){
        List<Integer> results = dataTransactionRepositoryOracle.getAccEnabled(username);

        if (results.isEmpty()) {
            return null;
        }

        Integer accEnabled = results.get(0);

        // Column exists but NULL
        if (accEnabled == null) {
            return null;
        }

        return accEnabled; // 0 or 1
    }


    public boolean validatePassword(String username, String password) {
        try {
            String result = dataTransactionRepositoryOracle.login(username, password);
            return result != null;
        } catch (Exception e) {
            log.error("Error validating password for user {}: {}", username, e.getMessage(), e);
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) {
        try {
            dataTransactionRepositoryOracle.updatePassword(username, newPassword);
            log.info("Password updated successfully for user: {}", username);
            return true;
        } catch (Exception e) {
            log.error("Error changing password for user {}: {}", username, e.getMessage(), e);
            return false;
        }
    }

}
