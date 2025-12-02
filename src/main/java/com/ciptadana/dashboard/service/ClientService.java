package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.backoffice.repository.ClientRepository;
import com.ciptadana.dashboard.database.backoffice.repository.dto.ClientDTO;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientProjection;
import com.ciptadana.dashboard.database.oracle.repository.CashPositionRepository;
import com.ciptadana.dashboard.database.oracle.repository.PortfolioRepositoryOracle;
import com.ciptadana.dashboard.database.oracle.repository.dto.PortfolioDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.CashPositionProjection;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.PortfolioListProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CashPositionRepository cashPositionRepository;

    public ClientDTO getClientDetail(String clientCode) {
        ClientProjection clientDetail = clientRepository.getClientDetail(clientCode);

        cashPositionRepository.executeApiClientBuyingPower(clientCode, "");
        String clientrating = cashPositionRepository.getClientRating(); // Anda sudah mendapatkan rating di sini

        ClientDTO dto = new ClientDTO();
        if (clientDetail != null) {
            BeanUtils.copyProperties(clientDetail, dto);
        }

        dto.setClientrating(clientrating);

        return dto;
    }
}
