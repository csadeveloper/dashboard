package com.ciptadana.dashboard.database.backoffice.repository.jpa;

import com.ciptadana.dashboard.database.backoffice.entity.NativeEntity;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientAvgProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientAvgJpaRepository extends JpaRepository<NativeEntity, String> {
    @Query(value = "SELECT  NCLIENT, NSHARE, PRICE FROM BOGOR.TEMP_CLIENT_DATA@DBCORE", nativeQuery = true)
    List<ClientAvgProjection> findAllByClient();

    @Query(value = "SELECT NVL(PRICE, 0) TOTAL   FROM BOGOR.TEMP_CLIENT_DATA@DBCORE WHERE ROWNUM = 1", nativeQuery = true)
    String findSinglePrice();
}
