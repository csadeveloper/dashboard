package com.ciptadana.dashboard.database.oracle.repository.jpa;

import com.ciptadana.dashboard.database.oracle.entity.NativeEntity;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTransactionJpaRepositoryOracle extends JpaRepository<NativeEntity, String> {


    @Query(value =  "SELECT FULLNAME FROM CORE.APP_USER " +
                    "WHERE USERNAME = :username AND PASSWORD1 = SHA1(:password) ", nativeQuery = true)
    String login(@Param("username") String username,
                 @Param("password") String password
                 );


    @Query(value =  "SELECT A.USERNAME, FULLNAME CLIENTNAME,A.CLIENTCODE,SESSION_ID SESSIONID, UPDATED_TIME UPDATEDTIME FROM APP_USER A " +
                    "LEFT JOIN APP_SESS_DASHBOARD B ON A.USERNAME = B.USERNAME " +
                    "WHERE SESSION_ID = :sessionId ", nativeQuery = true)
    LoginResult getSessionStatus(@Param("sessionId") String sessionId);

    @Modifying
    @Transactional
    @Query(value =  "INSERT INTO CORE.APP_SESS_DASHBOARD(SESSION_ID, CREATED_TIME, USERNAME, STATUS, UPDATED_TIME) " +
                    "VALUES (:sessionId, :createdTime, :username, :statusLogin, :updatedTime)", nativeQuery = true)
    void insertSessionLogin(@Param("sessionId") String sessionId,
                              @Param("createdTime") String createdTime,
                              @Param("username") String username,
                              @Param("statusLogin") int statusLogin,
                              @Param("updatedTime") String updatedTime
    );

    @Modifying
    @Transactional
    @Query(value =  "UPDATE CORE.APP_SESS_DASHBOARD SET STATUS = :statusLogin, UPDATED_TIME = :updatedTime " +
                    "WHERE SESSION_ID = :sessionId AND USERNAME = :username ", nativeQuery = true)
    void updateSessionLogout(@Param("sessionId") String sessionId,
                               @Param("username") String username,
                               @Param("statusLogin") int statusLogin,
                               @Param("updatedTime") String updatedTime
    );

}
