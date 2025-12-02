package com.ciptadana.dashboard.database.oracle.repository.jpa;

import com.ciptadana.dashboard.database.oracle.entity.NativeEntity;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.PortfolioListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioJpaRepository extends JpaRepository<NativeEntity, String> {

    @Query(value = "SELECT * FROM V_GET_PORTFOLIO_LIST WHERE NCLIENT = :clientCode ORDER BY NSHARE", nativeQuery = true)
    List<PortfolioListProjection> getPortfolioList(@Param("clientCode") String clientCode);
}
