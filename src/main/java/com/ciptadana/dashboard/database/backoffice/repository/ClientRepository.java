package com.ciptadana.dashboard.database.backoffice.repository;

import com.ciptadana.dashboard.database.backoffice.repository.jpa.ClientJpaRepository;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientRepository {

    private final ClientJpaRepository clientJpaRepository;

    public ClientProjection getClientDetail(String clientCode) {
        return clientJpaRepository.getClientDetail(clientCode);
    }
}
