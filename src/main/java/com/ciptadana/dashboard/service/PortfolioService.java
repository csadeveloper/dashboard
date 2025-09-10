package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.oracle.repository.PortfolioRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.dto.PortfolioDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.PortfolioListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepositoryOracle portfolioRepository;

    /**
     * Method untuk mengambil daftar Portofolio.
     * Logikanya tetap sama.
     */
    public List<PortfolioDTO> getPortfolioList(String clientCode) {
        List<PortfolioListProjection> portfolioItems = portfolioRepository.getPortfolioList(clientCode);
        return portfolioItems.stream()
                .map(item -> {
                    PortfolioDTO dto = new PortfolioDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).collect(Collectors.toList());
    }
}
