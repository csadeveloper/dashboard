package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.oracle.repository.dto.CashPositionDTO;
import com.ciptadana.dashboard.service.CashPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cash-position")
@RequiredArgsConstructor
public class CashPositionController {

    private final CashPositionService cashPositionService;

    @GetMapping("/{clientCode}")
    public ResponseEntity<CashPositionDTO> getCashPosition(@PathVariable String clientCode) {
        CashPositionDTO data = cashPositionService.getCashPosition(clientCode);
        return ResponseEntity.ok(data);
    }
}
