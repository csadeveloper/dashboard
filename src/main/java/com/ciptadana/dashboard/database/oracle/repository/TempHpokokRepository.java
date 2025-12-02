package com.ciptadana.dashboard.database.oracle.repository;

import com.ciptadana.dashboard.database.oracle.repository.jpa.TempHpokokJpaRepository;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.TempHpokokProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TempHpokokRepository {

    private final TempHpokokJpaRepository tempHpokokJpaRepository;

    public void executePortofolioMovement(String date, String clientCode, String res) {
        tempHpokokJpaRepository.executePortofolioMovement(date, clientCode, res);
    }

    public List<TempHpokokProjection> getTempHpokok(String clientCode) {
        return tempHpokokJpaRepository.findAllByClient(clientCode);
    }

}
