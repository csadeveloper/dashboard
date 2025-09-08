package com.ciptadana.dashboard.database.oracle.repository.jpa.projection;

public interface LoginResult {

    String getUsername();
    String getClientName();
    String getSessionId();
    String getUpdatedTime();
}
