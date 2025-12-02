package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.backoffice.repository.dto.ClientDTO;
import com.ciptadana.dashboard.database.oracle.repository.dto.PortfolioDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.exception.ResourceNotFoundException;
import com.ciptadana.dashboard.exception.UnauthorizedException;
import com.ciptadana.dashboard.service.ClientService;
import com.ciptadana.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final DashboardService sessionService;

    @GetMapping("/{clientCode}")
    public ResponseEntity<ClientDTO> getClientDetail(
            @PathVariable String clientCode,
            @RequestHeader("Authorization") String authorizationHeader) {

        String sessionId = authorizationHeader.replace("Bearer ", "").trim();

        // ✅ Validasi session
        LoginResult session = sessionService.validateActiveSession(sessionId);
        if (session == null || session.getStatus() != 0) {
            throw new UnauthorizedException("Session is invalid or expired");
        }

        if (!session.getClientCode().equals(clientCode)) {
            throw new UnauthorizedException("Client code mismatch");
        }

        // ✅ Session valid → ambil data
        ClientDTO data = clientService.getClientDetail(clientCode);
        if (data == null) {
            throw new ResourceNotFoundException("Client data tidak ditemukan");
        }
        return ResponseEntity.ok(data);
    }
}
