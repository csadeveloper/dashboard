package com.ciptadana.dashboard.database.backoffice.repository.jpa;

import com.ciptadana.dashboard.database.backoffice.entity.NativeEntity;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientJpaRepository extends JpaRepository<NativeEntity, String> {

//    @Query(value = "SELECT a.BANK_ACCOUNT_NO AS BANKACCOUNTNO, A.KSEI_SUB_REK AS SRE, A.SID , b.NAME AS CLIENTTYPE " +
//            "FROM CLIENT a JOIN CLIENT_TYPE b ON a.CLIENT_TYPE = b.ID " +
//            "WHERE code = :clientcode ", nativeQuery = true)

    //DEV getClientDetail *delete _DEV
    @Query(value = "SELECT c.BANK_ACC_NO_INVESTOR AS BANKACCOUNTNO, A.KSEI_SUB_REK AS SRE, A.SID , b.NAME AS CLIENTTYPE " +
            "FROM CLIENT@BOS a JOIN CLIENT_TYPE@BOS b ON a.CLIENT_TYPE = b.ID  JOIN  CLIENT_PROPERTY@BOS c ON a.CODE = c.CLIENT_ID " +
            "WHERE code = :clientcode ", nativeQuery = true)
    ClientProjection getClientDetail_DEV(@Param("clientcode") String clientcode);

    //PROD getClientDetail *delete _PROD
    @Query(value = "SELECT c.BANK_ACC_NO_INVESTOR AS BANKACCOUNTNO, A.KSEI_SUB_REK AS SRE, A.SID , b.NAME AS CLIENTTYPE " +
            "FROM CLIENT a JOIN CLIENT_TYPE b ON a.CLIENT_TYPE = b.ID  JOIN  CLIENT_PROPERTY c ON a.CODE = c.CLIENT_ID " +
            "WHERE code = :clientcode ", nativeQuery = true)
    ClientProjection getClientDetail(@Param("clientcode") String clientcode);
}
