package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.backoffice.repository.ClientAvgRepository;
import com.ciptadana.dashboard.database.backoffice.repository.dto.ClientAvgDTO;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientAvgProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientAvgService {

    private final ClientAvgRepository clientAvgRepository;

    @Transactional("backofficeTransactionManager")
    public List<ClientAvgDTO> getClientAverage(String clientCode, String nshare) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        log.info("[ClientAvgService] Getting client average - Date: {}, Client: {}, Share: {}",
                today, clientCode, nshare);

        try {
            // Execute SP dan ambil data
            List<ClientAvgProjection> projections = clientAvgRepository.getClientData(
                    today, clientCode, nshare
            );

            log.info("[ClientAvgService] Found {} records", projections.size());

            // Map ke DTO
            List<ClientAvgDTO> results = projections.stream()
                    .map(p -> {
                        ClientAvgDTO dto = new ClientAvgDTO();
                        dto.setNclient(p.getNclient());
                        dto.setNshare(p.getNshare());
                        dto.setPrice(p.getPrice());
                        return dto;
                    })
                    .toList();

            // Log results
            if (!results.isEmpty()) {
                log.info("[ClientAvgService] Sample result -> Client: {}, Share: {}, Price: {}",
                        results.get(0).getNclient(),
                        results.get(0).getNshare(),
                        results.get(0).getPrice()
                );
            }

            return results;

        } catch (Exception e) {
            log.error("[ClientAvgService] Failed to get client average for client: {}", clientCode, e);
            throw new RuntimeException("Error fetching client average: " + e.getMessage(), e);
        }
    }
}