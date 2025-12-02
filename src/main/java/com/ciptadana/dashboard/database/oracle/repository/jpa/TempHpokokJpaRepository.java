package com.ciptadana.dashboard.database.oracle.repository.jpa;

import com.ciptadana.dashboard.database.oracle.entity.NativeEntity;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.TempHpokokProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempHpokokJpaRepository extends JpaRepository<NativeEntity, String> {
    @Procedure(procedureName = "BOGOR.PORTFOLIOMOVEMENT")
    void executePortofolioMovement(
            @Param("P_DATE") String date,
            @Param("P_CLIENT_CODE") String clientCode,
            @Param("RES") String res
    );

    // Ambil semua data dari TEMP_HPOKOK sesuai NCLIENT
    @Query(value = "SELECT NCLIENT, NSHARE, PRICE FROM BOGOR.TEMP_HPOKOK WHERE NCLIENT = :clientCode ORDER BY NCLIENT, NSHARE, PRICE", nativeQuery = true)
    List<TempHpokokProjection> findAllByClient(@Param("clientCode") String clientCode);

}
