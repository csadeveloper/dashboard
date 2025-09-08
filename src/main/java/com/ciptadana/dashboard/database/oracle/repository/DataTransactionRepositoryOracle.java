package com.ciptadana.dashboard.database.oracle.repository;

import com.ciptadana.dashboard.database.oracle.repository.jpa.DataTransactionJpaRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataTransactionRepositoryOracle {

    private final DataTransactionJpaRepositoryOracle dataTransactionJpaRepositoryOracle;

    public String login(String username, String password){

        return dataTransactionJpaRepositoryOracle.login(username, username+password);
    }

    public LoginResult getSessionStatus(String sessionId){

        return dataTransactionJpaRepositoryOracle.getSessionStatus(sessionId);
    }

    public void insertSessionLogin(String sessionId, String createdTime, String username, int statusLogin, String updatedTime){

        dataTransactionJpaRepositoryOracle.insertSessionLogin(sessionId, createdTime, username, statusLogin, updatedTime);
    }

    public void updateSessionLogout(String sessionId, String username, int statusLogin, String updatedTime){

        dataTransactionJpaRepositoryOracle.updateSessionLogout(sessionId, username, statusLogin, updatedTime);
    }

}
