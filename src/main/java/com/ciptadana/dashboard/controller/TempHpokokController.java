package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.oracle.repository.dto.TempHpokokDTO;
import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.exception.ResourceNotFoundException;
import com.ciptadana.dashboard.exception.UnauthorizedException;
import com.ciptadana.dashboard.service.DashboardService;
import com.ciptadana.dashboard.service.TempHpokokService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tmp-hpokok")
@RequiredArgsConstructor
public class TempHpokokController {

    private final TempHpokokService tempHpokokService;
    private final DashboardService sessionService;

    @GetMapping("/{clientCode}")
    public ResponseEntity<List<TempHpokokDTO>> getTempHpokok(@PathVariable String clientCode,
                                                             @RequestHeader("Authorization") String authorizationHeader) {

        System.out.println("Authorization getTempHpokok: " + authorizationHeader);

        // Extract sessionId dari Authorization header
        String sessionId = authorizationHeader.replace("Bearer ", "").trim();

        LoginResult session = sessionService.validateActiveSession(sessionId);
        if (session == null || session.getStatus() != 0) {
            throw new UnauthorizedException("Session is invalid or expired");
        }

        if (!session.getClientCode().equals(clientCode)) {
            throw new UnauthorizedException("Client code mismatch");
        }

        // data dari service
        List<TempHpokokDTO> data = tempHpokokService.getTempHpokok(clientCode);
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("Data portfolio tidak ditemukan");
        }
        return ResponseEntity.ok(data);
    }
}