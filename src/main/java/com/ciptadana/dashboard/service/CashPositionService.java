package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.oracle.repository.CashPositionRepository;
import com.ciptadana.dashboard.database.oracle.repository.dto.CashPositionDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.CashPositionProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPositionService {

    private final CashPositionRepository cashPositionRepository;

    public CashPositionDTO getCashPosition(String clientCode) {
        // 1. Jalankan stored procedure
        cashPositionRepository.executeApiClientBuyingPower(clientCode, "");

        // 2. Ambil data dari view
        CashPositionProjection projection = cashPositionRepository.getCashPosition();

        // 3. Salin data dari projection ke DTO
        CashPositionDTO dto = new CashPositionDTO();
        if (projection != null) {
            BeanUtils.copyProperties(projection, dto);
        }

        return dto;
    }
}