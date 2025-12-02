package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.backoffice.repository.dto.ClientAvgDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.exception.ResourceNotFoundException;
import com.ciptadana.dashboard.exception.UnauthorizedException;
import com.ciptadana.dashboard.service.ClientAvgService;
import com.ciptadana.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avg-price")
@RequiredArgsConstructor
@Slf4j
public class ClientAvgController {

    private final ClientAvgService clientAvgService;
    private final DashboardService sessionService;

    @GetMapping("/{clientCode}/{nshare}")
    public ResponseEntity<List<ClientAvgDTO>> getClientAvg(
            @PathVariable String clientCode,
            @PathVariable String nshare,
            @RequestHeader("Authorization") String authorizationHeader) {

        log.info("[ClientAvgController] Request received - Client: {}, Share: {}", clientCode, nshare);

        // 1️⃣ Validate session
        String sessionId = authorizationHeader.replace("Bearer ", "").trim();
        LoginResult session = sessionService.validateActiveSession(sessionId);

        if (session == null || session.getStatus() != 0) {
            log.warn("[ClientAvgController] Invalid session: {}", sessionId);
            throw new UnauthorizedException("Session is invalid or expired");
        }

        if (!session.getClientCode().equals(clientCode)) {
            log.warn("[ClientAvgController] Client code mismatch - Expected: {}, Got: {}",
                    session.getClientCode(), clientCode);
            throw new UnauthorizedException("Client code mismatch");
        }

        // 2️⃣ Fetch data
        List<ClientAvgDTO> data = clientAvgService.getClientAverage(clientCode, nshare);

        // 3️⃣ Check if empty
        if (data == null || data.isEmpty()) {
            log.warn("[ClientAvgController] No data found for client: {}, share: {}", clientCode, nshare);
            throw new ResourceNotFoundException(
                    String.format("Data average price tidak ditemukan untuk client: %s, share: %s",
                            clientCode, nshare)
            );
        }

        log.info("[ClientAvgController] Returning {} records", data.size());
        return ResponseEntity.ok(data);
    }
}
