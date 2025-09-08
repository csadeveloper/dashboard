package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.oracle.repository.DataTransactionRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {


    private int STATUS_LOGIN  = 0;
    private int STATUS_LOGOUT = 1;
    private final DataTransactionRepositoryOracle dataTransactionRepositoryOracle;

    public LoginResult login(String username, String password){

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
    }

    public boolean logout(String sessionId, String username){

        boolean result = false;
        try {

            LocalDateTime today = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss:SSS");
            String createdTime = today.format(formatter);

            dataTransactionRepositoryOracle.updateSessionLogout(sessionId,
                                                                username,
                                                                STATUS_LOGOUT,
                                                                today.format(formatter));
            result = true;

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return result;
    }

    private String generateNumericSession(int length) {
        Random random = new Random();
        StringBuilder session = new StringBuilder();

        for (int i = 0; i < length; i++) {
            session.append(random.nextInt(10));
        }

        return session.toString();
    }

}
