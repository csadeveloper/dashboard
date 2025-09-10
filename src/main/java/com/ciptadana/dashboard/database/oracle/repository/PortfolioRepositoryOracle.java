package com.ciptadana.dashboard.database.oracle.repository;

import com.ciptadana.dashboard.database.oracle.repository.jpa.PortfolioJpaRepository;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.PortfolioListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PortfolioRepositoryOracle {
    private final PortfolioJpaRepository portfolioJpaRepository;

    public List<PortfolioListProjection> getPortfolioList(String clientCode) {
        return portfolioJpaRepository.getPortfolioList(clientCode);
    }
}
