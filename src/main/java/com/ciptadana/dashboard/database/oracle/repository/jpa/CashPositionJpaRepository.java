package com.ciptadana.dashboard.database.oracle.repository.jpa;

import com.ciptadana.dashboard.database.oracle.entity.NativeEntity;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.CashPositionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashPositionJpaRepository extends JpaRepository<NativeEntity, String> {

    @Procedure(procedureName = "BOGOR.API_CLIENTBUYINGPOWER")
    void executeApiClientBuyingPower(@Param("P_CLIENT_CODE") String clientCode, @Param("RES") String res);

    @Query(value = "SELECT * FROM V_BP_FO WHERE ROWNUM = 1", nativeQuery = true)
    CashPositionProjection getCashPosition();
}