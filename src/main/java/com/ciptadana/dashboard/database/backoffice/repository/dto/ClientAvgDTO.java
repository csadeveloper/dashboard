package com.ciptadana.dashboard.database.backoffice.repository.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientAvgDTO {
    private String nclient;
    private String nshare;
    private BigDecimal price;
}
