package com.ciptadana.dashboard.service;

import com.ciptadana.dashboard.database.oracle.repository.TempHpokokRepository;
import com.ciptadana.dashboard.database.oracle.repository.dto.CashPositionDTO;
import com.ciptadana.dashboard.database.oracle.repository.dto.TempHpokokDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.CashPositionProjection;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.TempHpokokProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TempHpokokService {
    private final TempHpokokRepository tempHpokokRepository;

    public List<TempHpokokDTO> getTempHpokok(String clientCode) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("getTempHpokok " + today + " " + clientCode);

        // 1. Jalankan stored procedure
        tempHpokokRepository.executePortofolioMovement(today, clientCode, "");

        // 2. Ambil semua data
        List<TempHpokokProjection> projections = tempHpokokRepository.getTempHpokok(clientCode);

        // 3. Mapping ke DTO list
        return projections.stream().map(p -> {
            TempHpokokDTO dto = new TempHpokokDTO();
            dto.setNclient(p.getNclient());
            dto.setNshare(p.getNshare());
            dto.setPrice(p.getPrice());
            return dto;
        }).toList();
    }
}

