package com.ciptadana.dashboard.database.backoffice.repository;

import com.ciptadana.dashboard.database.backoffice.repository.jpa.ClientAvgJpaRepository;
import com.ciptadana.dashboard.database.backoffice.repository.jpa.projection.ClientAvgProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientAvgRepository {

    private final ClientAvgJpaRepository clientAvgJpaRepository;

    @PersistenceContext(unitName = "backoffice")
    private EntityManager entityManager;

    @Transactional("backofficeTransactionManager")
    public List<ClientAvgProjection> getClientData(String date, String clientCode, String nshare) {
        try {
            log.info("[ClientAvgRepository] Setting schema to DENPASAR...");

            // 1️⃣ Set schema
            entityManager.createNativeQuery("ALTER SESSION SET CURRENT_SCHEMA = DENPASAR")
                    .executeUpdate();

            log.info("[ClientAvgRepository] Executing stored procedure...");

            // 2️⃣ Execute SP
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("GET_CLIENTAVG_AS_OF_SP")
                    .registerStoredProcedureParameter("MDATE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("MCLIENTCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("MSTOCKCODE", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("RES", String.class, ParameterMode.OUT)
                    .setParameter("MDATE", date)
                    .setParameter("MCLIENTCODE", clientCode)
                    .setParameter("MSTOCKCODE", nshare);

            query.execute();
            String result = (String) query.getOutputParameterValue("RES");
            log.info("[ClientAvgRepository] SP Result: {}", result);

            // 3️⃣ Flush untuk ensure SP commit
            entityManager.flush();

            // 4️⃣ Baru ambil data dari TEMP_CLIENT_DATA (masih dalam session yang sama!)
            List<ClientAvgProjection> data = clientAvgJpaRepository.findAllByClient();
            log.info("[ClientAvgRepository] Data size from @DBCORE: {}", data.size());

            return data;

        } catch (Exception e) {
            log.error("[ClientAvgRepository] Error executing procedure", e);
            throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
        }
    }

    @Transactional(value = "backofficeTransactionManager", readOnly = true)
    public String getAvgPrice() {
        return clientAvgJpaRepository.findSinglePrice();
    }
}


